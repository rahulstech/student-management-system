package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rahulstech.javafx.studentmanagementsystem.model.Sex;
import rahulstech.javafx.studentmanagementsystem.model.Student;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StudentDaoTest extends BaseDBTest {

    @Test
    void addStudent() {
        Student student = new Student();
        student.setGivenName("Rohan");
        student.setFamilyName("Sen");
        student.setSex(Sex.MALE);
        student.setDateOfBirth(LocalDate.of(1990,12,12));
        student.setPhotoURL("/photos/students/photo_of_rohan.jpg");
        student.setAddress("Behala, Kolkata");
        student.setPhone("+91-774-598-5623");
        student.setEmail("rohan1990@gmail.com");
        student.setDateOfJoin(LocalDate.of(2021,1,10));

        Student created = getDb().getStudentDao().addStudent(student);

        assertEquals("STUD20214",created.getStudentId(),"invalid student id of new student");
    }

    @ParameterizedTest
    @MethodSource
    void getStudentByStudentId(String testName, String studentId, Student expected) {
        Student actual = getDb().getStudentDao().getStudentByStudentId(studentId,null);
        if (null == actual) {
            assertNull(expected,testName+": null returned");
        }
        else {
            assertEquals(expected.getStudentId(),actual.getStudentId(),testName+": student id");
            assertEquals(expected.getGivenName(),actual.getGivenName(),testName+": given name");
            assertEquals(expected.getFamilyName(),actual.getFamilyName(),testName+": family name");
            assertEquals(expected.getSex(),actual.getSex(),testName+": sex");
            assertEquals(expected.getPhotoURL(),actual.getPhotoURL(),testName+": photo url");
            assertEquals(expected.getDateOfBirth(),actual.getDateOfBirth(),testName+": date of birth");
            assertEquals(expected.getAddress(),actual.getAddress(),testName+": address");
            assertEquals(expected.getPhone(),actual.getPhone(),testName+": phone");
            assertEquals(expected.getEmail(),actual.getEmail(),testName+": email");
            assertEquals(expected.getDateOfJoin(),actual.getDateOfJoin(),testName+": date of join");
        }
    }

    private static Stream<Arguments> getStudentByStudentId() {
        return Stream.of(
                Arguments.of("Existing Student","STUD20213",
                        createStudent("STUD20213","Roxy","Ghosh",Sex.OTHER,
                                "photos/students/photo4.jpg",LocalDate.of(1994,2,27),
                                "Khagra, Berhampore, Murshidabad","+91 959 329 0045","roxxxxxy1997@gmail.com",
                                LocalDate.of(2021,1,10))),
                Arguments.of("Non-Existing Student","STUD1245",null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void addStudent_Exception(String testName, Student student, Class<Throwable> expected) {
        assertThrows(expected,() -> getDb().getStudentDao().addStudent(student),testName);
    }

    private static Stream<Arguments> addStudent_Exception() {
        return Stream.of(
                Arguments.of("Null Value For NonNull Column", new Student(),DatabaseException.class)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStudentByStudentId_LimitedColumns(String testName, String studentId, String[] columns, Student expected) {
        Student actual = getDb().getStudentDao().getStudentByStudentId(studentId,columns);
        if (null == actual) {
            assertNull(expected,testName+": null returned");
        }
        else {
            assertEquals(expected.getStudentId(),actual.getStudentId(),testName+": student id");
            assertEquals(expected.getGivenName(),actual.getGivenName(),testName+": given name");
            assertEquals(expected.getFamilyName(),actual.getFamilyName(),testName+": family name");
            assertEquals(expected.getSex(),actual.getSex(),testName+": sex");
            assertEquals(expected.getPhotoURL(),actual.getPhotoURL(),testName+": photo url");
            assertEquals(expected.getDateOfBirth(),actual.getDateOfBirth(),testName+": date of birth");
            assertEquals(expected.getAddress(),actual.getAddress(),testName+": address");
            assertEquals(expected.getPhone(),actual.getPhone(),testName+": phone");
            assertEquals(expected.getEmail(),actual.getEmail(),testName+": email");
            assertEquals(expected.getDateOfJoin(),actual.getDateOfJoin(),testName+": date of join");
        }
    }

    private static Stream<Arguments> getStudentByStudentId_LimitedColumns() {
        return Stream.of(
                Arguments.of("O Length Columns List","STUD20213",
                        new String[0],
                        createStudent("STUD20213","Roxy","Ghosh",Sex.OTHER,
                                "photos/students/photo4.jpg",LocalDate.of(1994,2,27),
                                "Khagra, Berhampore, Murshidabad","+91 959 329 0045","roxxxxxy1997@gmail.com",
                                LocalDate.of(2021,1,10))),
                Arguments.of("A Set of Columns List","STUD20213",
                        new String[]{"student_id","given_name","family_name","date_of_birth","phone"},
                        createStudent("STUD20213","Roxy","Ghosh",null,
                        null,LocalDate.of(1994,2,27),
                        null,"+91 959 329 0045",null,
                        null))
        );
    }

    @ParameterizedTest
    @MethodSource
    void updateStudent(String testName, Student student, boolean expected) {
        boolean actual = getDb().getStudentDao().updateStudent(student);
        assertEquals(expected,actual,testName);
    }

    private static Stream<Arguments> updateStudent() {
        return Stream.of(
                Arguments.of("Existing Student",
                        createStudent("STUD20212","Payel","Roy",
                                Sex.FEMALE,"photos/students/photo7.jpg",LocalDate.of(1997,5,19),
                                "Kandi Bus Stand, Kandi Murshidabad","+91 899 138 9996","roy.payel@gmail.com",
                                LocalDate.of(2021,1,5))
                        ,true),
                Arguments.of("Non-Existing Student",
                        createStudent("STUD4578","Om","Roy",Sex.OTHER,"photos/students/photo78.jpg",
                                LocalDate.of(1997,5,22),
                                "Kandi College Road, Kandi Murshidabad","+91 899 140 9856","roy.payel@gmail.com",
                                LocalDate.of(2021,1,5))
                        ,false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void updateStudent_Exception(String testName, Student student, Class<Throwable> expected) {
        assertThrows(expected,() -> getDb().getStudentDao().updateStudent(student),testName);
    }

    private static Stream<Arguments> updateStudent_Exception() {
        return Stream.of(
                Arguments.of("Update with Null values",
                        createStudent("STUD20212",null,null,null,
                                null,null,null,null,null,null)
                        ,DatabaseException.class)
        );
    }

    private static Student createStudent(String studentId, String givenName, String familyName,
                                         Sex sex, String photoURL, LocalDate dateOfBirth,
                                         String address, String phone, String email, LocalDate dateOfJoin) {
        Student s = new Student();
        s.setStudentId(studentId);
        s.setGivenName(givenName);
        s.setFamilyName(familyName);
        s.setSex(sex);
        s.setPhotoURL(photoURL);
        s.setDateOfBirth(dateOfBirth);
        s.setAddress(address);
        s.setPhone(phone);
        s.setEmail(email);
        s.setDateOfJoin(dateOfJoin);
        return s;
    }
}