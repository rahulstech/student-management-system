package rahulstech.javafx.studentmanagementsystem.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rahulstech.javafx.studentmanagementsystem.model.Course;
import rahulstech.javafx.studentmanagementsystem.model.CourseStatus;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static rahulstech.javafx.studentmanagementsystem.util.Helpers.arrayToString;
import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isEmptyArray;

public class CourseDao {

    private static final Logger logger = LogManager.getLogger(CourseDao.class);

    private StudentDB db;

    CourseDao(StudentDB db) {
        this.db = db;
    }

    public Course addCourse(Course course) {
        String sql = "INSERT INTO courses (course_id,name,description,fees,status," +
                "duration_years,course_start,course_end,total_seats,available_seats) VALUES (?,?,?,?,?,?,?,?,?);";
        Course copy = course.clone();
        copy.setCourseId(generateNewCourseId());
        logger.debug("sql: "+sql+" | values: ["+copy+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,copy.getCourseId());
            ps.setString(2,copy.getName());
            ps.setString(3,copy.getDescription());
            ps.setFloat(4,copy.getFees());
            ps.setString(5,copy.getStatus().getValue());
            ps.setDate(6, Date.valueOf(copy.getCourseStart()));
            ps.setDate(7,Date.valueOf(copy.getCourseEnd()));
            ps.setInt(8,course.getTotalSeats());
            ps.setInt(9,course.getAvailableSeats());
            ps.execute();
            return copy;
        }
        catch (Exception e) {
            logger.error("addCourse",e);
            throw new DatabaseException(e);
        }
    }

    public List<Course> getAllCoursesWithStatus(CourseStatus status) {
        String sql = "SELECT * FROM courses WHERE status = ?;";
        logger.debug("sql: "+sql+" | value: [status="+status+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,status.getValue());
            ResultSet rs = ps.executeQuery();
            if (null != rs) {
                List<Course> courses = new ArrayList<>();
                while (rs.next()) {
                    Course course = newCourse(rs);
                    courses.add(course);
                }
                return courses;
            }
            return Collections.emptyList();
        }
        catch (Exception e) {
            logger.error("getAllCoursesWithStatus",e);
            throw new DatabaseException(e);
        }
    }

    public Course getCourseById(String courseId, String[] columns) {
        String sql = "SELECT ";
        sql += isEmptyArray(columns) ? "*" : arrayToString(columns,",");
        sql += " FROM courses WHERE course_id = ?;";
        logger.debug("sql: "+sql+" | values: [courseId=\""+courseId+"\"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,courseId);
            ResultSet rs = ps.executeQuery();
            if (null != rs && rs.next()) {
                Course course = isEmptyArray(columns) ? newCourse(rs) : newCourse(rs,columns);
                return course;
            }
            return null;
        }
        catch (Exception e) {
            logger.error("getAllCoursesWithStatus",e);
            throw new DatabaseException(e);
        }
    }

    /**
     *
     * @param courseId
     * @param status
     * @return
     */
    public boolean changeCourseStatus(String courseId, CourseStatus status) {
        String sql = "UPDATE courses SET status = ? WHERE course_id = ?;";
        logger.debug("sql: "+sql+" | values: [courseId=\""+courseId+"\", status="+status+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,status.getValue());
            ps.setString(2,courseId);
            return ps.executeUpdate() > 0;
        }
        catch (Exception e) {
            logger.error("changeCourseStatus",e);
            throw new DatabaseException(e);
        }
    }

    public boolean updateCourseInfo(Course course) {
        String sql = "UPDATE courses SET name = ?, description = ?, fees = ?, status = ?," +
                " course_start = ?, course_end = ?, total_seats = ?, available_seats = ? WHERE course_id = ?;";
        logger.debug("sql: "+sql+" | values: ["+course+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,course.getName());
            ps.setString(2,course.getDescription());
            ps.setFloat(3,course.getFees());
            ps.setString(4,course.getStatus().getValue());
            ps.setDate(5,Date.valueOf(course.getCourseStart()));
            ps.setDate(6,Date.valueOf(course.getCourseEnd()));
            ps.setInt(7,course.getTotalSeats());
            ps.setInt(8,course.getAvailableSeats());
            ps.setString(9,course.getCourseId());
            return ps.executeUpdate() > 0;
        }
        catch (Exception e) {
            logger.error("updateCourseInfo",e);
            throw new DatabaseException(e);
        }
    }

    private String generateNewCourseId() {
        String tableName = "courses";
        String pattern = "C"+ LocalDate.now().getYear();
        return db.generateAndGetId(tableName,pattern);
    }

    private Course newCourse(ResultSet rs) throws Exception {
        Course course = new Course();
        course.setCourseId(rs.getString("course_id"));
        course.setName(rs.getString("name"));
        course.setDescription(rs.getString("description"));
        course.setFees(rs.getFloat("fees"));
        course.setStatus(CourseStatus.from(rs.getString("status")));
        course.setCourseStart(rs.getDate("course_start").toLocalDate());
        course.setCourseEnd(rs.getDate("course_end").toLocalDate());
        course.setTotalSeats(rs.getInt("total_seats"));
        course.setAvailableSeats(rs.getInt("available_seats"));
        return course;
    }

    private Course newCourse(ResultSet rs, String[] columns) throws Exception {
        Course course = new Course();
        for (String col : columns) {
            switch (col) {
                case "course_id": course.setCourseId(rs.getString("course_id"));
                break;
                case "name": course.setName(rs.getString("name"));
                break;
                case "description": course.setDescription(rs.getString("description"));
                break;
                case "fees": course.setFees(rs.getFloat("fees"));
                break;
                case "status": course.setStatus(CourseStatus.from(rs.getString("status")));
                break;
                case "course_start": course.setCourseStart(rs.getDate("course_start").toLocalDate());
                break;
                case "course_end": course.setCourseEnd(rs.getDate("course_end").toLocalDate());
                break;
                case "total_seats": course.setTotalSeats(rs.getInt("total_seats"));
                break;
                case "available_seats": course.setAvailableSeats(rs.getInt("available_seats"));
                break;
            }
        }
        return course;
    }
}
