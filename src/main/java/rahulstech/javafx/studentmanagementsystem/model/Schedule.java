package rahulstech.javafx.studentmanagementsystem.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Schedule {

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
        Schedule that = (Schedule) o;
        return Objects.equals(scheduleId, that.scheduleId);
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
