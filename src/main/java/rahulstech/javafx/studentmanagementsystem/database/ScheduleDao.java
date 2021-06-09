package rahulstech.javafx.studentmanagementsystem.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rahulstech.javafx.studentmanagementsystem.model.Course;
import rahulstech.javafx.studentmanagementsystem.model.Schedule;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ScheduleDao {

    private static final Logger logger = LogManager.getLogger(Schedule.class);

    private StudentDB db;

    ScheduleDao(StudentDB db) {
        this.db = db;
    }

    /**
     * Create a new schedule for a course.
     * <strong>Note:</strong> schedules can be created for
     * coursed which are not canceled or complete yet i.e.
     * course status is neither of
     * {@link rahulstech.javafx.studentmanagementsystem.model.CourseStatus#COURSE_CANCEL COURSE_CANCEL}
     * nor {@link rahulstech.javafx.studentmanagementsystem.model.CourseStatus#COURSE_COMPLETE COURSE_COMPLETE}.
     * Called need to check the above conditions first then calle this method
     *
     * @param schedule
     * @return
     */
    public Schedule createSchedule(Schedule schedule) {
        String sql = "INSERT INTO schedules (schedule_id,course_id,start,end,description) " +
                "VALUES (?,?,?,?,?);";
        Schedule copy = schedule.clone();
        copy.setScheduleId(generateNewScheduleId());
        logger.debug("sql: "+sql+" | values ["+copy+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,copy.getScheduleId());
            ps.setString(2,copy.getCourse().getCourseId());
            ps.setTimestamp(3, Timestamp.valueOf(schedule.getStart()));
            ps.setTimestamp(4,Timestamp.valueOf(schedule.getEnd()));
            ps.setString(5,schedule.getDescription());
            ps.execute();
            return copy;
        }
        catch (Exception e) {
            logger.error("createSchedule",e);
            throw new DatabaseException(e);
        }
    }

    public boolean checkOverlappingSchedule(LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT COUNT(schedule_id) FROM schedules WHERE (start BETWEEN ? AND ?) OR (end BETWEEN ? AND ?);";
        logger.debug("sql: "+sql+" | values: [start="+start+", end="+end+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1,Timestamp.valueOf(start));
            ps.setTimestamp(2,Timestamp.valueOf(end));
            ps.setTimestamp(3,Timestamp.valueOf(start));
            ps.setTimestamp(4,Timestamp.valueOf(end));
            ResultSet rs = ps.executeQuery();
            if (null != rs && rs.next()) {
                long count = rs.getLong(1);
                return count > 0;
            }
            throw new DatabaseException("unable to get query result");
        }
        catch (Exception e) {
            logger.error("checkOverlappingSchedule",e);
            throw new DatabaseException(e);
        }
    }

    public List<Schedule> getAllSchedulesForCourse(String courseId) {
        String sql = "SELECT * FROM schedules WHERE course_id = ?;";
        logger.debug("sql: "+sql+" | values: [courseId=\""+courseId+"\"]");
        Connection conn = db.getConnection();
        Course course = db.getCourseDao().getCourseById(courseId,new String[]{"course_id","name","status"});
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,courseId);
            ResultSet rs = ps.executeQuery();
            if (null != rs) {
                List<Schedule> schedules = new ArrayList<>();
                while (rs.next()) {
                    Schedule schedule = newSchedule(rs,course);
                    schedules.add(schedule);
                }
                return schedules;
            }
            return Collections.emptyList();
        }
        catch (Exception e) {
            logger.error("getAllSchedulesForCourse",e);
            throw new DatabaseException(e);
        }
    }

    public List<Schedule> getAllSchedulesForDate(LocalDate date) {
        String sql = "SELECT * FROM schedules WHERE DATE(start) = ?;";
        logger.debug("sql: "+sql+" values: [date="+date+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1,Date.valueOf(date));
            ResultSet rs = ps.getResultSet();
            if (null != rs) {
                List<Schedule> schedules = new ArrayList<>();
                while (rs.next()) {
                    String courseId = rs.getString("course_id");
                    Course course = db.getCourseDao().getCourseById(courseId,new String[]{"course_id","name","status"});
                    Schedule schedule = newSchedule(rs,course);
                    schedules.add(schedule);
                }
                return schedules;
            }
            return Collections.emptyList();
        }
        catch (Exception e) {
            logger.error("getAllScheduleForDate",e);
            throw new DatabaseException(e);
        }
    }

    public List<Schedule> getStudentSchedulesForDate(String studentId, LocalDate date) {
        String sql = "SELECT schedules.* FROM schedules INNER JOIN admissions ON schedules.course_id = admissions.course_id " +
                "WHERE admissions.student_id = ? AND DATE(schedules.start) = ?;";
        logger.debug("sql: "+sql+" values: [studentId=\""+studentId+"\", date="+date+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1,Date.valueOf(date));
            ResultSet rs = ps.getResultSet();
            if (null != rs) {
                List<Schedule> schedules = new ArrayList<>();
                while (rs.next()) {
                    String courseId = rs.getString("course_id");
                    Course course = db.getCourseDao().getCourseById(courseId,new String[]{"course_id","name","status"});
                    Schedule schedule = newSchedule(rs,course);
                    schedules.add(schedule);
                }
                return schedules;
            }
            return Collections.emptyList();
        }
        catch (Exception e) {
            logger.error("getAllScheduleForDate",e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Delete a schedule.
     * Only future schedules can be deleted.
     * Caller need to verify that first before calling this method
     *
     * @param scheduleId
     * @return
     */
    public boolean deleteSchedule(String scheduleId) {
        String sql = "DELETE FROM schedules WHERE schedule_id = ?;";
        logger.debug("sql: "+sql+" values: [scheduleId=\""+scheduleId+"\"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,scheduleId);
            return ps.executeUpdate() > 0;
        }
        catch (Exception e) {
            logger.error("deleteSchedule",e);
            throw new DatabaseException(e);
        }
    }

    private String generateNewScheduleId() {
        return UUID.randomUUID().toString();
    }

    private Schedule newSchedule(ResultSet rs, Course course) throws Exception {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(rs.getString("schedule_id"));
        schedule.setCourse(course);
        schedule.setStart(rs.getTimestamp("start").toLocalDateTime());
        schedule.setEnd(rs.getTimestamp("end").toLocalDateTime());
        schedule.setDescription(rs.getString("description"));
        return schedule;
    }
}
