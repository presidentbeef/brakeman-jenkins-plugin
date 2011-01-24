package hudson.plugins.brakeman;

/**
 * Represents the result summary of the warnings parser. This summary will be
 * shown in the summary.jelly script of the warnings result action.
 *
 * @author Maximilian Odendahl
 */
public final class ResultSummary {
    /**
     * Returns the message to show as the result summary.
     *
     * @param result
     *            the result
     * @return the message
     */
    public static String createSummary(final BrakemanResult result) {
        StringBuilder summary = new StringBuilder();
        int bugs = result.getNumberOfAnnotations();

        summary.append(Messages.Brakeman_ProjectAction_Name());
        summary.append(": ");
        if (bugs > 0) {
            summary.append("<a href=\"brakemanResult\">");
        }
        if (bugs == 1) {
            summary.append(Messages.Brakeman_ResultAction_OneWarning());
        }
        else {
            summary.append(Messages.Brakeman_ResultAction_MultipleWarnings(bugs));
        }
        if (bugs > 0) {
            summary.append("</a>");
        }
        summary.append(".");
        return summary.toString();
    }

    /**
     * Returns the message to show as the result summary.
     *
     * @param result
     *            the result
     * @return the message
     */
    public static String createDeltaMessage(final BrakemanResult result) {
        StringBuilder summary = new StringBuilder();
        if (result.getNumberOfNewWarnings() > 0) {
            summary.append("<li><a href=\"brakemanResult/new\">");
            if (result.getNumberOfNewWarnings() == 1) {
                summary.append(Messages.Brakeman_ResultAction_OneNewWarning());
            }
            else {
                summary.append(Messages.Brakeman_ResultAction_MultipleNewWarnings(result.getNumberOfNewWarnings()));
            }
            summary.append("</a></li>");
        }
        if (result.getNumberOfFixedWarnings() > 0) {
            summary.append("<li><a href=\"brakemanResult/fixed\">");
            if (result.getNumberOfFixedWarnings() == 1) {
                summary.append(Messages.Brakeman_ResultAction_OneFixedWarning());
            }
            else {
                summary.append(Messages.Brakeman_ResultAction_MultipleFixedWarnings(result.getNumberOfFixedWarnings()));
            }
            summary.append("</a></li>");
        }

        return summary.toString();
    }

    /**
     * Instantiates a new result summary.
     */
    private ResultSummary() {
        // prevents instantiation
    }
}

