package seers.bug_report_analysis.tools;

public class Issue1 {
    String bugID;
    String title;
    String openTime;
    String closedTime;
    String description;


    @Override
    public String toString() {
        return "is{" +
                "bi='" + bugID + '\'' +
                ", t='" + title + '\'' +
                ", ot='" + openTime + '\'' +
                ", ct='" + closedTime + '\'' +
                ", d='" + description + '\'' +
                '}';
    }
}
