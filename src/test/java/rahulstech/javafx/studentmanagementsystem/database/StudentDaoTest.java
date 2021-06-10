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
import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isContentEqual;

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

        assertDoesNotThrow(()->{
            Student created = getDb().getStudentDao().addStudent(student);
            String newStudentId = "STUD"+LocalDate.now().getYear()+"6";
            assertEquals(newStudentId,created.getStudentId(),"unexpectd student id returned for created student");
        },"create student exception thrown for correct values");
    }

    @ParameterizedTest
    @MethodSource
    void getStudentByStudentId(String testName, String studentId, Student expected) {
        Student actual = getDb().getStudentDao().getStudentByStudentId(studentId,null);
        if (null == actual) {
            assertNull(expected,testName+": non null returned");
        }
        else {
            assertTrue(isContentEqual(expected,actual),"getStudentByStudentId content not equal");
        }
    }

    private static Stream<Arguments> getStudentByStudentId() {
        return Stream.of(
                Arguments.of("Existing Student","STUD20213",
                        createStudent("STUD20213","gn8","fn8",Sex.OTHER,
                                "photo8",LocalDate.of(1998,12,3),
                                "address8","phone8","email8",LocalDate.of(2021,4,5))),
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
        if (null == expected) {
            assertNull(actual,testName+": not null returned");
        }
        else {
            assertTrue(isContentEqual(expected,actual),testName+": content not quals");
        }
    }

    private static Stream<Arguments> getStudentByStudentId_LimitedColumns() {
        return Stream.of(
                Arguments.of("O Length Columns List","STUD20213",
                        new String[0],
                        createStudent("STUD20213","gn8","fn8",Sex.OTHER,
                                "photo8",LocalDate.of(1998,12,3),
                                "address8","phone8","email8",LocalDate.of(2021,4,5))
                ),
                Arguments.of("A Set of Columns List","STUD20213",
                        new String[]{"student_id","given_name","family_name","date_of_birth","phone"},
                        createStudent("STUD20213","gn8","fn8",null,
                                null,LocalDate.of(1998,12,3),
                                null,"phone8",null,null)
                ),
                Arguments.of("Non Existing Student Record","STUD20221",
                        new String[]{"student_id","given_name","family_name","date_of_birth","phone"},
                        null
                )
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