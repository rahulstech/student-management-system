package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rahulstech.javafx.studentmanagementsystem.model.Course;
import rahulstech.javafx.studentmanagementsystem.model.CourseStatus;
import rahulstech.javafx.studentmanagementsystem.model.Schedule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isContentEqual;

class ScheduleDaoTest extends BaseDBTest {


    @Test
    void createSchedule() {
    }

    @Test
    void checkOverlappingSchedule() {
    }

    @ParameterizedTest
    @MethodSource
    void getAllSchedulesForCourse(String testName, String courseId, List<Schedule> expected) {
        List<Schedule> actual = getDb().getBatchScheduleDao().getAllSchedulesForCourse(courseId);
        assertEquals(expected.size(),actual.size(),testName+": different size");
        for (int i = 0; i < expected.size(); i++) {
            Schedule ei = expected.get(i);
            Schedule ai = actual.get(i);
            assertTrue(isContentEqual(ei,ai),testName+": content not equal");
        }
    }

    private static Stream<Arguments> getAllSchedulesForCourse() {
        return Stream.of(
                Arguments.of("","", Arrays.asList())
        );
    }

    @Test
    void getAllSchedulesForDate() {
    }

    @Test
    void getStudentSchedulesForDate() {
    }

    @Test
    void deleteSchedule() {
    }

    private static Schedule newSchedule(String schedule_id,
                                        Course course,
                                        LocalDateTime start,LocalDateTime end,
                                        String description) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(schedule_id);
        schedule.setCourse(course);
        schedule.setStart(start);
        schedule.setEnd(end);
        schedule.setDescription(description);
        return schedule;
    }

    private static Course newCourse(String course_id, String name, String status) {
        Course course = new Course();
        course.setCourseId(course_id);
        course.setName(name);
        course.setStatus(null == status ? null : CourseStatus.from(status));
        return course;
    }
}