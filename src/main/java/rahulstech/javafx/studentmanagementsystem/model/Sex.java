package rahulstech.javafx.studentmanagementsystem.model;

public enum Sex {
    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");

    private String value;

    Sex(String value) {
        this.value = value;
    }

    public static Sex from(String value) {
        switch (value) {
            case "MALE": return MALE;
            case "FEMALE": return FEMALE;
            case "OTHER": return OTHER;
        }
        throw new IllegalArgumentException(value+" is not a value valid SEX");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Sex{" +
                "value='" + value + '\'' +
                "} ";
    }
}
