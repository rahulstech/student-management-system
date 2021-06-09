package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rahulstech.javafx.studentmanagementsystem.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isContentEqual;

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
        if (null != expected) {
            assertNotNull(actual,testName+": null returned");
            assertEquals(expected.size(),actual.size(),testName+": different size");
            for (int i = 0; i < expected.size(); i++) {
                Admission expectedItem = expected.get(i);
                Admission actualItem = actual.get(i);
                assertTrue(isContentEqual(expectedItem,actualItem),testName+": content not equal");
            }
        }
        else {
            assertNull(actual,testName+": not null returned");
        }
    }

    private static Stream<Arguments> getPendingPaymentsForCourse() {
        return Stream.of(
                Arguments.of("Course With Pending Payments","C20202",
                        Arrays.asList(
                                newAdmission("A3",
                                        newStudent("STUD20201","gn1","fn1",
                                                "address1","phone1","email1"),
                                        newCourse("C20202",null,null),
                                        LocalDate.of(2020,12,1),AdmissionStatus.ENROLLED,
                                        160,20),
                                newAdmission("A6",
                                        newStudent("STUD20205","gn9","fn9",
                                                "address9","phone9","email9"),
                                        newCourse("C20202",null,null),
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
        if (null != expected) {
            assertNotNull(actual,testName+": null returned");
            assertEquals(expected.size(),actual.size(),testName+": different size");
            for (int i = 0; i < expected.size(); i++) {
                Admission expectedItem = expected.get(i);
                Admission actualItem = actual.get(i);

                assertTrue(isContentEqual(expectedItem,actualItem),testName+": content not equal");
            }
        }
        else {
            assertNull(actual,testName+": not null returned");
        }
    }

    private static Stream<Arguments> getPendingPaymentsForStudent() {
        return Stream.of(
                Arguments.of("Student With Pending Payments","STUD20201",
                        Arrays.asList(
                                newAdmission("A3",
                                        newStudent("STUD20201",null,null,null,null,null),
                                        newCourse("C20202","name 2","RUNNING"),
                                        LocalDate.of(2020,12,1),AdmissionStatus.ENROLLED,160,20),
                                newAdmission("A18",
                                        newStudent("STUD20201",null,null,null,null,null),
                                        newCourse("C20213","name 6","RUNNING"),
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

    private static Admission newAdmission(String admission_id, Student student, Course course,
                                          LocalDate admission_date, AdmissionStatus status,
                                          float net_payable, float due_payment) {
        Admission admission = new Admission();
        admission.setAdmissionId(admission_id);
        admission.setStudent(student);
        admission.setCourse(course);
        admission.setAdmissionDate(admission_date);
        admission.setStatus(status);
        admission.setNetPayable(net_payable);
        admission.setDuePayment(due_payment);
        return admission;
    }

    private static Course newCourse(String course_id, String name, String status) {
        Course course = new Course();
        course.setCourseId(course_id);
        course.setName(name);
        course.setStatus(null == status ? null : CourseStatus.from(status));
        return course;
    }

    private static Student newStudent(String student_id, String given_name, String family_name,
                                      String address, String phone, String email) {
        Student student = new Student();
        student.setStudentId(student_id);
        student.setGivenName(given_name);
        student.setFamilyName(family_name);
        student.setAddress(address);
        student.setPhone(phone);
        student.setEmail(email);
        return student;
    }
}