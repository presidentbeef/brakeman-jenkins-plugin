package hudson.plugins.brakeman;

import hudson.Extension;
import hudson.Launcher;
import hudson.Proc;
import hudson.FilePath;
import hudson.Launcher.LocalLauncher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.HealthAwarePublisher;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.util.PluginLogger;
import hudson.plugins.analysis.util.model.Priority;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Publishes the results of the warnings analysis (freestyle project type).
 *
 * @author Maximilian Odendahl
 */
// CHECKSTYLE:COUPLING-OFF
public class BrakemanPublisher extends HealthAwarePublisher {

	/** Unique ID of this class. */
	private static final long serialVersionUID = -5936973521277401765L;
	/** Descriptor of this publisher. */
	@Extension
	public static final BrakemanDescriptor BRAKEMAN_DESCRIPTOR = new BrakemanDescriptor();
	public String sourcecodedir;
	public String executable;
	private static Pattern pattern = Pattern.compile("^([^\t]+?)\t(\\d+)\t([\\w\\s]+?)\t(\\w+)\t([\\w\\s]+?)\t(High|Medium|Weak)", Pattern.MULTILINE);

	/**
	 * Creates a new instance of <code>WarningPublisher</code>.
	 *
	 * @param threshold
	 *            Annotation threshold to be reached if a build should be
	 *            considered as unstable.
	 * @param newThreshold
	 *            New annotations threshold to be reached if a build should be
	 *            considered as unstable.
	 * @param failureThreshold
	 *            Annotation threshold to be reached if a build should be
	 *            considered as failure.
	 * @param newFailureThreshold
	 *            New annotations threshold to be reached if a build should be
	 *            considered as failure.
	 * @param healthy
	 *            Report health as 100% when the number of annotations is less
	 *            than this value
	 * @param unHealthy
	 *            Report health as 0% when the number of annotations is greater
	 *            than this value
	 * @param thresholdLimit
	 *            determines which warning priorities should be considered when
	 *            evaluating the build stability and health
	 * @param defaultEncoding
	 *            the default encoding to be used when reading and parsing files
	 * @param useDeltaValues
	 *            determines whether the absolute annotations delta or the
	 *            actual annotations set difference should be used to evaluate
	 *            the build stability
	 */
	// CHECKSTYLE:OFF
	@SuppressWarnings("PMD.ExcessiveParameterList")
		@DataBoundConstructor
		public BrakemanPublisher(final String threshold, final String newThreshold,
				final String failureThreshold, final String newFailureThreshold,
				final String healthy, final String unHealthy, final String thresholdLimit,
				final String defaultEncoding, final String sourcecodedir, final String executable,
				final boolean useDeltaValues) {
			super(threshold, newThreshold, failureThreshold, newFailureThreshold,
					healthy, unHealthy, thresholdLimit, "UTF-8", useDeltaValues, "BRAKEMAN");

			this.sourcecodedir = sourcecodedir;
			this.executable = executable;
		}
	// CHECKSTYLE:ON

	/**
	 * Creates a new parser set for old versions of this class.
	 *
	 * @return this
	 */
	@Override
		protected Object readResolve() {
			super.readResolve();
			return this;
		}

	/** {@inheritDoc} */
	@Override
		public Action getProjectAction(final AbstractProject<?, ?> project) {
			return new BrakemanProjectAction(project);
		}

	/** {@inheritDoc} */
	@Override
		public BuildResult perform(final AbstractBuild<?, ?> build, final PluginLogger logger) throws InterruptedException, IOException {


			FilePath ws = build.getWorkspace();
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try {
				Launcher launcher = new LocalLauncher(TaskListener.NULL);
				Launcher.ProcStarter starter = launcher.launch();
				Proc proc = launcher.launch(starter.cmds("brakeman", "-f", "tabs").stdout(out).envs(build.getEnvironment()).pwd(ws));
				proc.join();
			} catch (Exception e) {
				logger.log(e);
				return null;
			}

			ParserResult project = new ParserResult(build.getWorkspace());
			this.scan(out.toString(), project);

			BrakemanResult result = new BrakemanResult(build, getDefaultEncoding(), project);
			build.getActions().add(new BrakemanResultAction(build, this, result));

			return result;
		}

	/** {@inheritDoc} */
	@Override
		public BuildStepDescriptor<Publisher> getDescriptor() {
			return BRAKEMAN_DESCRIPTOR;
		}

	/** {@inheritDoc} */
	@Override
		protected boolean canContinue(final Result result) {
			return super.canContinue(result);
		}

	 private void scan(String brakemanOutput, ParserResult project) {
		Matcher m = this.pattern.matcher(brakemanOutput);

		while(m.find()) {
			String fileName = m.group(1);
			int line = Integer.parseInt(m.group(2));
			String type = m.group(3);
			String category = m.group(4);
			String message = m.group(5);
			String prio = m.group(6);

			Priority priority = Priority.HIGH;
			if ("Medium".equalsIgnoreCase(prio)) {
				priority = Priority.NORMAL;
			} else if ("High".equalsIgnoreCase(prio)) {
				priority = Priority.HIGH;
			} else if ("Weak".equalsIgnoreCase(prio)) {
				priority = Priority.LOW;
			}

			int start = 0;
			int end = line + 1;

			if(line > 2)
				start = line - 1;
			

			project.addAnnotation(new Warning(fileName, start, end, type, category, message, priority));
		}
	 }

}
