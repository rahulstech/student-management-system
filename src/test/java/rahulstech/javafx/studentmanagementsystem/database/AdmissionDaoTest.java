package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rahulstech.javafx.studentmanagementsystem.model.Admission;
import rahulstech.javafx.studentmanagementsystem.model.AdmissionStatus;
import rahulstech.javafx.studentmanagementsystem.model.Course;
import rahulstech.javafx.studentmanagementsystem.model.Student;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AdmissionDaoTest extends BaseDBTest {

    @Test
    void admit() {
        Admission admission = newAdmission(null,"STUD20215","C20213",
                LocalDate.of(2021,4,6), AdmissionStatus.ENROLLED,
                200,0);
        assertDoesNotThrow(() -> {
            Admission created = getDb().getAdmissionDao().admit(admission);
            assertNotNull(created.getAdmissionId(),"null admission id new admission");
        });
    }

    @ParameterizedTest
    @MethodSource
    void admit_Exception(String testName, Admission admission, Class<Throwable> expected) {
        assertThrows(expected,() -> getDb().getAdmissionDao().admit(admission),testName);
    }

    private static Stream<Arguments> admit_Exception() {
        return Stream.of(
                Arguments.of("Null Values For NonNull fields", new Admission(),DatabaseException.class),
                Arguments.of("Admit Non Existing Student",newAdmission(null,"xyz","C20213",
                        LocalDate.of(2021,4,6), AdmissionStatus.ENROLLED,
                        200,0),DatabaseException.class),
                Arguments.of("Admit to Non Existing Course",newAdmission(null,"STUD20215","xyz",
                        LocalDate.of(2021,4,6), AdmissionStatus.ENROLLED,
                        200,0),DatabaseException.class)
        );
    }

    @ParameterizedTest
    @MethodSource
    void disEnrollStudent(String testName, String studentId, String courseId, boolean expected) {
        boolean actual = getDb().getAdmissionDao().disEnrollStudent(studentId,courseId);
        assertEquals(expected,actual,testName);
    }

    private static Stream<Arguments> disEnrollStudent() {
        return Stream.of(
                Arguments.of("ENROLLED student","STUD20205","C20203",true),
                Arguments.of("DISENROLLED student","STUD20212","C20213",true),
                Arguments.of("Disenroll non existing","xyz","abc",false)
        );
    }


    @ParameterizedTest
    @MethodSource
    void getPendingPaymentsForCourse(String testName, String courseId, List<Admission> expected) {
        List<Admission> actual = getDb().getAdmissionDao().getPendingPaymentsForCourse(courseId);
        assertEquals(expected,actual,testName);
    }

    private static Stream<Arguments> getPendingPaymentsForCourse() {
        return Stream.of(
                Arguments.of("Course With Pending Payments","C20202",
                        Arrays.asList(
                                newAdmission("A3","STUD20201","C20202",
                                        LocalDate.of(2020,12,1),AdmissionStatus.ENROLLED,
                                        160,20),
                                newAdmission("A6","STUD20205","C20202",
                                        LocalDate.of(2020,12,10),AdmissionStatus.ENROLLED,
                                        160,60))),
                Arguments.of("Course With No Pending Payments","C20201", Arrays.asList()),
                Arguments.of("Non Existing Course ID","xyz",Arrays.asList())
        );
    }

    @ParameterizedTest
    @MethodSource
    void getPendingPaymentsForStudent(String testName, String studentId, List<Admission> expected) {
        List<Admission> actual = getDb().getAdmissionDao().getPendingPaymentsForStudent(studentId);
        assertEquals(expected,actual,testName);
    }

    private static Stream<Arguments> getPendingPaymentsForStudent() {
        return Stream.of(
                Arguments.of("Student With Pending Payments","STUD20201",
                        Arrays.asList(
                                newAdmission("A3","STUD20201","C20202",
                                        LocalDate.of(2020,12,1),AdmissionStatus.ENROLLED,160,20),
                                newAdmission("A18","STUD20201","C20213",
                                        LocalDate.of(2020,3,12),AdmissionStatus.ENROLLED,130,30)
                        )),
                Arguments.of("Student With No Pending Payments","C20211",Arrays.asList()),
                Arguments.of("Non Existing Student Id","xyz",Arrays.asList())
        );
    }

    private static Admission newAdmission(String admission_id, String student_id, String course_id,
                                          LocalDate admission_date, AdmissionStatus status,
                                          float net_payable, float due_payment) {
        Admission admission = new Admission();
        admission.setAdmissionId(admission_id);
        Student student = new Student();
        student.setStudentId(student_id);
        admission.setStudent(student);
        Course course = new Course();
        course.setCourseId(course_id);
        admission.setCourse(course);
        admission.setAdmissionDate(admission_date);
        admission.setStatus(status);
        admission.setNetPayable(net_payable);
        admission.setDuePayment(due_payment);
        return admission;
    }
}