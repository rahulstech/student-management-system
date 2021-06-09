package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rahulstech.javafx.studentmanagementsystem.model.Admission;
import rahulstech.javafx.studentmanagementsystem.model.Course;
import rahulstech.javafx.studentmanagementsystem.model.Payment;
import rahulstech.javafx.studentmanagementsystem.model.Student;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDaoTest extends BaseDBTest {

    @Test
    void makePayment() {
        Payment payment = newPayment(null,"A3","STUD20201","C20202",
                LocalDateTime.of(2021,4, 25, 11,0,0),20);
        assertDoesNotThrow(() -> getDb().getPaymentDao().makePayment(payment));
    }

    @ParameterizedTest
    @MethodSource
    void makePayment_Exception(String testName, Payment payment, Class<Throwable> expected) {
        assertThrows(expected,() -> getDb().getPaymentDao().makePayment(payment),testName);
    }

    private static Stream<Arguments> makePayment_Exception() {
        return Stream.of(
                Arguments.of("Null values for non null fields", new Payment(), DatabaseException.class),
                Arguments.of("Foreign Key Exception admissionId",
                        newPayment(null,"STUD20201","A1000","C20202",
                                LocalDateTime.of(2020,3,30,11,0,0),40),
                        DatabaseException.class)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStudentPaymentHistoryForCourse(String testName, String studentId, String courseId, List<Payment> expected) {
        List<Payment> actual = getDb().getPaymentDao().getStudentPaymentHistoryForCourse(studentId,courseId);
        assertEquals(expected,actual,testName);
    }

    private static Stream<Arguments> getStudentPaymentHistoryForCourse() {
        return Stream.of(
                Arguments.of("Existing Payment History",
                        "STUD20201",
                        "C20202",
                        Arrays.asList(
                                newPayment("P3", "A3","STUD20201","C20202",
                                        LocalDateTime.of(2020,12,01,11,0,0),70),
                                newPayment("P4","A3","STUD20201","C20202",
                                        LocalDateTime.of(2020,1,20,11,0,0),30),
                                newPayment("P5","STUD20201","A3","C20202",
                                        LocalDateTime.of(2020,3,30,11,0,0),40)
                        )
                ),
                Arguments.of("Non Existing Payment History",
                        "STUD20215","C20213",Arrays.asList())
        );
    }

    private static Payment newPayment(String payment_id, String admission_id, String student_id, String course_id,
                                      LocalDateTime when, float amount) {
        Payment payment = new Payment();
        payment.setPaymentId(payment_id);
        Admission admission = new Admission();
        admission.setAdmissionId(admission_id);
        payment.setAdmission(admission);
        Student student = new Student();
        student.setStudentId(student_id);
        payment.setStudent(student);
        Course course = new Course();
        course.setCourseId(course_id);
        payment.setCourse(course);
        payment.setWhen(when);
        payment.setAmount(amount);
        return payment;
    }
}