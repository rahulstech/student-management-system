package rahulstech.javafx.studentmanagementsystem.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rahulstech.javafx.studentmanagementsystem.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PaymentDao  {

    private static final Logger logger = LogManager.getLogger(PaymentDao.class);

    private StudentDB db;

    PaymentDao(StudentDB db) {
        this.db = db;
    }

    /**
     * Adds a new entry in payments table
     * <strong>Note:</strong> new payment records can only be created
     * for courses which are not canceled i.e. course status is not
     * {@link CourseStatus#COURSE_CANCEL COURSE_CANCEL}. Called of this
     * method need to check before calling this method.
     *
     * @param payment
     * @return
     */
    public Payment makePayment(Payment payment) {
        String sql = "INSERT INTO payments (payment_id,student_id,course_id,amount,payment_datetime,admission_id) " +
                "VALUES (?,?,?,?,?,?);";
        Payment copy = payment.clone();
        copy.setPaymentId(generateNewPaymentId());
        logger.debug("sql: "+sql+" | values: ["+payment+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,copy.getPaymentId());
            ps.setString(2,copy.getStudent().getStudentId());
            ps.setString(3,copy.getCourse().getCourseId());
            ps.setFloat(4,copy.getAmount());
            ps.setTimestamp(5, Timestamp.valueOf(payment.getWhen()));
            ps.setString(6,payment.getAdmission().getAdmissionId());
            ps.execute();
            return copy;
        }
        catch (Exception e) {
            logger.error("makePayment",e);
            throw new DatabaseException(e);
        }
    }

    public List<Payment> getStudentPaymentHistoryForCourse(String studentId, String courseId) {
        String sql = "SELECT * FROM payments WHERE student_id = ? AND course_id = ?;";
        logger.debug("sql: "+sql+" | values: [studentId=\""+studentId+"\", batchId=\""+courseId+"\"]");
        Connection conn = db.getConnection();
        Student student = new Student();
        student.setStudentId(studentId);
        Course course = new Course();
        course.setCourseId(courseId);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,studentId);
            ps.setString(2,courseId);
            ResultSet rs = ps.executeQuery();
            if (null != rs) {
                List<Payment> payments = new ArrayList<>();
                while (rs.next()) {
                    String admissionId = rs.getString("admission_id");
                    Admission admission = new Admission();
                    admission.setAdmissionId(admissionId);
                    Payment payment = newPayment(rs,admission,student,course);
                    payments.add(payment);
                }
                return payments;
            }
            return Collections.emptyList();
        }
        catch (Exception e) {
            logger.error("getStudentPaymentHistoryForCourse",e);
            throw new DatabaseException(e);
        }
    }

    private String generateNewPaymentId() {
        return UUID.randomUUID().toString();
    }

    private Payment newPayment(ResultSet rs, Admission admission, Student student, Course course) throws Exception {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getString("payment_id"));
        payment.setAdmission(admission);
        payment.setStudent(student);
        payment.setCourse(course);
        payment.setAmount(rs.getFloat("amount"));
        payment.setWhen(rs.getTimestamp("payment_datetime").toLocalDateTime());
        return payment;
    }
}
