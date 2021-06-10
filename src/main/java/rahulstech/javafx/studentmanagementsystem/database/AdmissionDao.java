package rahulstech.javafx.studentmanagementsystem.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rahulstech.javafx.studentmanagementsystem.model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static rahulstech.javafx.studentmanagementsystem.model.AdmissionStatus.DISENROLLED;
import static rahulstech.javafx.studentmanagementsystem.model.AdmissionStatus.ENROLLED;

public class AdmissionDao  {

    private static final Logger logger = LogManager.getLogger(AdmissionDao.class);

    private StudentDB db;

    AdmissionDao(StudentDB db) {
        this.db = db;
    }

    /**
     * Add a new admission.
     * <strong>Note:</strong>A student can only admit to a course which
     * status is {@link CourseStatus#ADMISSION_OPEN ADMISSION_OPEN}.
     * Caller need to check the above conditions first then call this method.
     *
     * @param admission
     * @return
     */
    public Admission admit(Admission admission) {
        String sql = "INSERT INTO admissions (admission_id, student_id, course_id, " +
                "admission_date, status, net_payable, due_payment) " +
                "VALUES (?,?,?,?,?,?,?);";
        Admission copy = admission.clone();
        copy.setAdmissionId(generateNewAdmissionId());
        logger.debug("sql: "+sql+" | values: ["+copy+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,copy.getAdmissionId());
            ps.setString(2,copy.getStudent().getStudentId());
            ps.setString(3,copy.getCourse().getCourseId());
            ps.setDate(4, Date.valueOf(copy.getAdmissionDate()));
            ps.setString(5,copy.getStatus().getValue());
            ps.setFloat(6,copy.getNetPayable());
            ps.setFloat(7,copy.getDuePayment());
            ps.execute();
            return copy;
        }
        catch (Exception e) {
            logger.error("admit",e);
            throw new DatabaseException(e);
        }
    }

    public List<Admission> getPendingPaymentsForCourse(String courseId) {
        String sql = "SELECT * FROM admissions WHERE course_id = ? AND due_payment > 0;";
        logger.debug("sql: "+sql+" | values: [courseId=\""+courseId+"\"]");
        Connection conn = db.getConnection();
        Course course = db.getCourseDao().getCourseById(courseId,new String[]{"course_id","name","status"});
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,courseId);
            ResultSet rs = ps.executeQuery();
            if (null != rs) {
                List<Admission> admissions = new ArrayList<>();
                while (rs.next()) {
                    String studentId = rs.getString("student_id");
                    Student student = db.getStudentDao().getStudentByStudentId(studentId,
                            new String[]{"student_id","given_name","family_name",
                                    "address","email","phone"});
                    Admission admission = newAdmission(rs,student,course);
                    admissions.add(admission);
                }
                return admissions;
            }
            return Collections.emptyList();
        }
        catch (Exception e) {
            logger.error("getStudentsWithDuePaymentsForBatch",e);
            throw new DatabaseException(e);
        }
    }

    public List<Admission> getPendingPaymentsForStudent(String studentId) {
        String sql = "SELECT * FROM admissions WHERE student_id = ? AND status = \""+ENROLLED.getValue()+"\" AND due_payment > 0;";
        logger.debug("sql: "+sql+" | values: [studentId=\""+studentId+"\"]");
        Connection conn = db.getConnection();
        Student student = db.getStudentDao().getStudentByStudentId(studentId,
                new String[]{"student_id","given_name","family_name",
                        "address","email","phone"});;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,studentId);
            ResultSet rs = ps.executeQuery();
            if (null != rs) {
                List<Admission> admissions = new ArrayList<>();
                while (rs.next()) {
                    String courseId = rs.getString("course_id");
                    Course course = db.getCourseDao().getCourseById(courseId,new String[]{"course_id","name","status"});
                    Admission admission = newAdmission(rs,student,course);
                    admissions.add(admission);
                }
                return admissions;
            }
            return Collections.emptyList();
        }
        catch (Exception e) {
            logger.error("getStudentsWithDuePaymentsForBatch",e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Set the admission status to {@link AdmissionStatus#DISENROLLED DISENROLLED}.
     * <strong>Note:</strong> A student can only be disenrolled if the course is not.
     * cancel or complete. Caller of this method need to manually check the course status
     * first
     *
     * @param studentId
     * @param courseId
     * @return
     */
    public boolean disEnrollStudent(String studentId, String courseId) {
        String sql = "UPDATE admissions SET status = \""+DISENROLLED.getValue()+"\" " +
                "WHERE student_id = ? AND course_id = ?;";
        logger.debug("sql: "+sql+" | values: [studentId=\""+studentId+"\", courseId=\""+courseId+"\"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,studentId);
            ps.setString(2,courseId);
            return ps.executeUpdate() > 0;
        }
        catch (Exception e) {
            logger.error("disEnrollStudent",e);
            throw new DatabaseException(e);
        }
    }

    private String generateNewAdmissionId() {
        return UUID.randomUUID().toString();
    }

    private Admission newAdmission(ResultSet rs, Student student, Course course) throws Exception {
        Admission admission = new Admission();
        admission.setAdmissionId(rs.getString("admission_id"));
        admission.setStudent(student);
        admission.setCourse(course);
        admission.setStatus(AdmissionStatus.from(rs.getString("status")));
        admission.setAdmissionDate(rs.getDate("admission_date").toLocalDate());
        admission.setNetPayable(rs.getFloat("net_payable"));
        admission.setDuePayment(rs.getFloat("due_payment"));
        return admission;
    }
}
