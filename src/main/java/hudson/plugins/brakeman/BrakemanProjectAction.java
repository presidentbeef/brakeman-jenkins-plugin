package hudson.plugins.brakeman;

import hudson.model.AbstractProject;
import hudson.plugins.analysis.core.AbstractProjectAction;

/**
 * Entry point to visualize the warnings trend graph in the project screen.
 * Drawing of the graph is delegated to the associated
 * {@link BrakemanResultAction}.
 *
 * @author Maximilian Odendahl
 */
public class BrakemanProjectAction extends AbstractProjectAction<BrakemanResultAction> {
    /** Unique identifier of this class. */
    private static final long serialVersionUID = -654316141132780562L;

    /**
     * Instantiates a new find bugs project action.
     *
     * @param project
     *            the project that owns this action
     */
    public BrakemanProjectAction(final AbstractProject<?, ?> project) {
        super(project, BrakemanResultAction.class, BrakemanPublisher.BRAKEMAN_DESCRIPTOR);
    }

    /** {@inheritDoc} */
    public String getDisplayName() {
        return Messages.Brakeman_ProjectAction_Name();
    }

    /** {@inheritDoc} */
    @Override
    public String getTrendName() {
        return Messages.Brakeman_Trend_Name();
    }
}

