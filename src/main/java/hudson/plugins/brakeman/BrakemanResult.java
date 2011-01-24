package hudson.plugins.brakeman;

import hudson.model.AbstractBuild;
import hudson.plugins.analysis.core.BuildHistory;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;

import com.thoughtworks.xstream.XStream;


/**
 * Represents the results of the warning analysis. One instance of this class is persisted for
 * each build via an XML file.
 *
 * @author Maximilian Odendahl
 */
public class BrakemanResult extends BuildResult {
    /** Unique identifier of this class. */
    private static final long serialVersionUID = -137460587767210579L;

    /**
     * Creates a new instance of {@link BrakemanResult}.
     *
     * @param build
     *            the current build as owner of this action
     * @param defaultEncoding
     *            the default encoding to be used when reading and parsing files
     * @param result
     *            the parsed result with all annotations
     */
    public BrakemanResult(final AbstractBuild<?, ?> build, final String defaultEncoding, final ParserResult result) {
        super(build, defaultEncoding, result);
    }

    /**
     * Creates a new instance of {@link BrakemanResult}.
     *
     * @param build
     *            the current build as owner of this action
     * @param defaultEncoding
     *            the default encoding to be used when reading and parsing files
     * @param result
     *            the parsed result with all annotations
     * @param history
     *            the plug-in history
     */
    protected BrakemanResult(final AbstractBuild<?, ?> build, final String defaultEncoding, final ParserResult result,
            final BuildHistory history) {
        super(build, defaultEncoding, result, history);
    }

    /** {@inheritDoc} */
    @Override
    protected void configure(final XStream xstream) {
        xstream.alias("warning", Warning.class);
    }

    /**
     * Returns a summary message for the summary.jelly file.
     *
     * @return the summary message
     */
    public String getSummary() {
        return ResultSummary.createSummary(this);
    }

    /** {@inheritDoc} */
    @Override
    protected String createDeltaMessage() {
        return ResultSummary.createDeltaMessage(this);
    }

    /** {@inheritDoc} */
    @Override
    protected String getSerializationFileName() {
        return "compiler-Brakeman.xml";
    }

    /** {@inheritDoc} */
    public String getDisplayName() {
        return Messages.Brakeman_ProjectAction_Name();
    }

    /** {@inheritDoc} */
    @Override
    protected Class<? extends ResultAction<? extends BuildResult>> getResultActionType() {
        return BrakemanResultAction.class;
    }
}
