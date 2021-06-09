package rahulstech.javafx.studentmanagementsystem.model;

import rahulstech.javafx.studentmanagementsystem.util.EqualsContent;

import java.time.LocalDateTime;
import java.util.Objects;

import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isContentEqual;

public class Schedule implements EqualsContent {

    private String scheduleId;
    private Course course;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;

    public Schedule() {}

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId='" + scheduleId + '\'' +
                ", course=" + course +
                ", start=" + start +
                ", end=" + end +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(scheduleId, schedule.scheduleId);
    }

    public boolean equalsContent(Object o) {
        if (null == o) return false;
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(scheduleId, schedule.scheduleId) &&
                isContentEqual(course,schedule.course) &&
                Objects.equals(start, schedule.start) &&
                Objects.equals(end, schedule.end) &&
                Objects.equals(description, schedule.description);
    }

    public Schedule clone() {
        Schedule copy = new Schedule();
        copy.scheduleId = this.scheduleId;
        copy.course = this.course;
        copy.start = this.start;
        copy.end = this.end;
        copy.description = this.description;
        return copy;
    }
}
