package rahulstech.javafx.studentmanagementsystem.model;

public enum AdmissionStatus {

    ENROLLED("ENROLLED"),
    DISENROLLED("DISENROLLED");

    private String value;

    AdmissionStatus(String value) {
        this.value = value;
    }

    public static AdmissionStatus from(String value) {
        switch (value) {
            case "ENROLLED": return ENROLLED;
            case "DISENROLLED": return DISENROLLED;
        }
        throw new IllegalArgumentException(value+" is not a valid AdmissionStatus");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AdmissionStatus{" +
                "value='" + value + '\'' +
                "} ";
    }
}
