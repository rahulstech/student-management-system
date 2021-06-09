package rahulstech.javafx.studentmanagementsystem.model;

import rahulstech.javafx.studentmanagementsystem.util.EqualsContent;

import java.time.LocalDate;
import java.util.Objects;

public class Course implements Cloneable, EqualsContent {

    private String courseId;
    private String name;
    private String description;
    private Float fees;
    private CourseStatus status;
    private LocalDate courseStart;
    private LocalDate courseEnd;
    private Integer totalSeats;
    private Integer availableSeats;

    public Course() {}

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getFees() {
        return fees;
    }

    public void setFees(Float fees) {
        this.fees = fees;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public LocalDate getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(LocalDate courseStart) {
        this.courseStart = courseStart;
    }

    public LocalDate getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(LocalDate courseEnd) {
        this.courseEnd = courseEnd;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", fees=" + fees +
                ", status=" + status +
                ", courseStart=" + courseStart +
                ", courseEnd=" + courseEnd +
                ", totalSeats=" + totalSeats +
                ", availableSeats=" + availableSeats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    public boolean equalsContent(Object o) {
        if (null == o) return false;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId) &&
                Objects.equals(name, course.name) &&
                Objects.equals(description, course.description) &&
                Objects.equals(fees, course.fees) &&
                status == course.status &&
                Objects.equals(courseStart, course.courseStart) &&
                Objects.equals(courseEnd, course.courseEnd) &&
                Objects.equals(totalSeats, course.totalSeats) &&
                Objects.equals(availableSeats, course.availableSeats);
    }

    public Course clone() {
        Course copy = new Course();
        copy.courseId = this.courseId;
        copy.name = this.name;
        copy.description = this.description;
        copy.fees = this.fees;
        copy.status = this.status;
        copy.courseStart = this.courseStart;
        copy.courseEnd = this.courseEnd;
        copy.totalSeats = this.totalSeats;
        copy.availableSeats = this.availableSeats;
        return copy;
    }
}
