package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rahulstech.javafx.studentmanagementsystem.model.Course;
import rahulstech.javafx.studentmanagementsystem.model.CourseStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rahulstech.javafx.studentmanagementsystem.model.CourseStatus.CLASSES_RUNNING;
import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isContentEqual;

class CourseDaoTest extends BaseDBTest {

    @Test
    void addCourse() {
    }

    @ParameterizedTest
    @MethodSource
    void getAllCoursesWithStatus(String testName, CourseStatus status, List<Course> expected) {
        List<Course> actual = getDb().getCourseDao().getAllCoursesWithStatus(status);
        assertEquals(expected.size(),actual.size(),testName+": different size");
        for (int i = 0; i < expected.size(); i++) {
            Course ei = expected.get(i);
            Course ai = actual.get(0);
            assertTrue(isContentEqual(ei,ai),testName+": content not equals");
        }
    }

    private static Stream<Arguments> getAllCoursesWithStatus() {
        return Stream.of(
                Arguments.of("RUNNING Courses",CLASSES_RUNNING, Arrays.asList())
        );
    }

    @Test
    void getCourseById() {
    }

    @Test
    void changeCourseStatus() {
    }

    @Test
    void updateCourseInfo() {
    }
}