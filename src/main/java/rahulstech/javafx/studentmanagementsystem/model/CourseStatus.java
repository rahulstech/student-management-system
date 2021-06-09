package rahulstech.javafx.studentmanagementsystem.model;

public enum CourseStatus {

    ADMISSION_OPEN("OPEN"),
    ADMISSION_CLOSE("CLOSE"),
    CLASSES_RUNNING("RUNNING"),
    COURSE_CANCEL("CANCEL"),
    COURSE_COMPLETE("COMPLETE");


    private String value;

    CourseStatus(String value) {
        this.value = value;
    }

    public static CourseStatus from(String value) {
        switch (value) {
            case "OPEN": return ADMISSION_OPEN;
            case "CLOSE": return ADMISSION_CLOSE;
            case "RUNNING": return CLASSES_RUNNING;
            case "COURSE_CANCEL": return COURSE_CANCEL;
            case "COURSE_COMPLETE": return COURSE_COMPLETE;
        }
        throw new IllegalArgumentException(value+" is not a valid course status");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CourseStatus{" +
                "value='" + value + '\'' +
                "} " + super.toString();
    }
}
