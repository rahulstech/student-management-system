package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rahulstech.javafx.studentmanagementsystem.model.Course;
import rahulstech.javafx.studentmanagementsystem.model.CourseStatus;
import rahulstech.javafx.studentmanagementsystem.model.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isContentEqual;

class ScheduleDaoTest extends BaseDBTest {


    @Test
    void createSchedule() {
        Schedule schedule = newSchedule(null,
                newCourse("C20213",null,null),
                LocalDateTime.of(2021,6,1,8,30,0),
                LocalDateTime.of(2021,6,1,10,0,0),
                "class 3");
        assertDoesNotThrow(() -> getDb().getScheduleDao().createSchedule(schedule),
                "createSchedule throws exception for valid values");
    }

    @ParameterizedTest
    @MethodSource
    void checkOverlappingSchedule(String testName, LocalDateTime start, LocalDateTime end, boolean expected) {
        boolean actual = getDb().getScheduleDao().checkOverlappingSchedule(start, end);
        assertEquals(expected,actual,testName);
    }

    private static Stream<Arguments> checkOverlappingSchedule() {
        return Stream.of(
                Arguments.of("Containing Schedule",
                        LocalDateTime.of(2021,6,1,9,0,0),
                        LocalDateTime.of(2021,6,1,10,0,0),
                        true),
                Arguments.of("Intersect Start",
                        LocalDateTime.of(2020,12,30,11,30,0),
                        LocalDateTime.of(2020,12,30,12,30,0),
                        true),
                Arguments.of("Intersect End",
                        LocalDateTime.of(2020,7,5 ,10,0,0),
                        LocalDateTime.of(2020,7,5,11,0,0),
                        true),
                Arguments.of("Non Overlapping",
                        LocalDateTime.of(2021,6,1,21,0,0),
                        LocalDateTime.of(2021,6,1,22,0,0),false),
                Arguments.of("Not overlaps with canceled course",
                        LocalDateTime.of(2020,4,15,7,0,0),
                        LocalDateTime.of(2020,4,14,8,0,0),
                        false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getAllSchedulesForCourse(String testName, String courseId, List<Schedule> expected) {
        List<Schedule> actual = getDb().getScheduleDao().getAllSchedulesForCourse(courseId);
        assertEqualsListOfSchedules(testName,expected,actual);
    }

    private static Stream<Arguments> getAllSchedulesForCourse() {
        return Stream.of(
                Arguments.of("Existing Schedule","C20201",
                        Arrays.asList(
                                newSchedule("S1",
                                        newCourse("C20201","name 1","COMPLETE"),
                                        LocalDateTime.of(2020,4,15,10,0,0),
                                        LocalDateTime.of(2020,4,15,11,0,0),
                                        "class 1"),
                                newSchedule("S2",
                                        newCourse("C20201","name 1","COMPLETE"),
                                        LocalDateTime.of(2020,6,12,10,0,0),
                                        LocalDateTime.of(2020,6,12,11,0,0),
                                        "class 2"),
                                newSchedule("S3",
                                        newCourse("C20201","name 1","COMPLETE"),
                                        LocalDateTime.of(2020,7,5,10,0,0),
                                        LocalDateTime.of(2020,7,5,11,0,0),
                                        "class 3")
                        )),
                Arguments.of("Non Existing Course","xyz", Arrays.asList())
        );
    }

    @ParameterizedTest
    @MethodSource
    void getAllSchedulesForDate(String testName, LocalDate date, List<Schedule> expected) {
        List<Schedule> actual = getDb().getScheduleDao().getAllSchedulesForDate(date);
        assertEqualsListOfSchedules(testName,expected,actual);
    }

    private static Stream<Arguments> getAllSchedulesForDate() {
        return Stream.of(
                Arguments.of("Exists Schedules On Date",
                        LocalDate.of(2020,4,15),
                        Arrays.asList(
                                newSchedule("S1",
                                        newCourse("C20201","name 1","COMPLETE"),
                                        LocalDateTime.of(2020,4,15,10,0),
                                        LocalDateTime.of(2020,4,15,11,0,0),
                                        "class 1"),
                                newSchedule("S7",
                                        newCourse("C20202","name 2","RUNNING"),
                                        LocalDateTime.of(2021,4,15,11,30,0),
                                        LocalDateTime.of(2021,4,15,12,30,0),
                                        "class 4"),
                                newSchedule("S10",newCourse("C20213","name 6","RUNNING"),
                                        LocalDateTime.of(2021,4,15,8,30,0),
                                        LocalDateTime.of(2021,4,15,10,30,0),
                                        "class 1"))
                        ),
                Arguments.of("No Schedules On Date",
                        LocalDate.of(2021,12,13),
                        Arrays.asList())
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStudentSchedulesForDate(String testName, String studentId, LocalDate date, List<Schedule> expected) {
        List<Schedule> actual = getDb().getScheduleDao().getStudentSchedulesForDate(studentId,date);
        assertEqualsListOfSchedules(testName,expected,actual);
    }

    private static Stream<Arguments> getStudentSchedulesForDate() {
        return Stream.of(
                Arguments.of("Student Schedules On Date Existing","STUD20201",
                        LocalDate.of(2020,4,15),
                        Arrays.asList(
                                newSchedule("S1",
                                        newCourse("C20201","name 1","COMPLETE"),
                                        LocalDateTime.of(2020,4,15,10,0),
                                        LocalDateTime.of(2020,4,15,11,0,0),
                                        "class 1")
                        )
                ),
                Arguments.of("Student Schedules On Date Non Existing","STUD20201",
                        LocalDate.of(2020,3,15),
                        Arrays.asList()
                ),
                Arguments.of("Non Admitted Student Schedules On Date","STUD20215",
                        LocalDate.of(2020,3,15),
                        Arrays.asList()
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void deleteSchedule(String testName, String scheduleId, boolean expected) {
        boolean actual = getDb().getScheduleDao().deleteSchedule(scheduleId);
        assertEquals(expected,actual,testName);
    }

    private static Stream<Arguments> deleteSchedule() {
        return Stream.of(
                Arguments.of("Delete Existing Schedule","S1",true),
                Arguments.of("Delete Non Existing Schedule","xyz",false)
        );
    }

    private static void assertEqualsListOfSchedules(String testName, List<Schedule> expected, List<Schedule> actual) {
        assertEquals(expected.size(),actual.size(),testName+": different size");
        for (int i = 0; i < expected.size(); i++) {
            Schedule ei = expected.get(i);
            Schedule ai = actual.get(i);
            assertTrue(isContentEqual(ei,ai),testName+": content not equal");
        }
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