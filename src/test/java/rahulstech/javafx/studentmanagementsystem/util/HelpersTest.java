package rahulstech.javafx.studentmanagementsystem.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HelpersTest {

    @ParameterizedTest
    @MethodSource
    void arrayToString(String testName, String[] array, String joiner, String expected) {
        String actual = Helpers.arrayToString(array,joiner);
        assertEquals(expected,actual,testName);
    }

    private static Stream<Arguments> arrayToString() {
        return Stream.of(
                Arguments.of("Null Array",null,",",null),
                Arguments.of("O Length Array",new String[0],",",null),
                Arguments.of("Single Item",new String[]{"Hello"},",","Hello"),
                Arguments.of("No Joiner",new String[]{"Hello","World"},null,"Hello,World")
        );
    }

    @ParameterizedTest
    @MethodSource
    void filterMatched(String testName, String[] src, String pattern, String[] expected) {
        String[] actual = Helpers.filterMatched(src,pattern);
        assertArrayEquals(expected,actual,testName);
    }

    private static Stream<Arguments> filterMatched() {
        return Stream.of(
                Arguments.of("No Match",
                        new String[]{"batch_id","total_seats","available_seats"},
                        "(batch\\.)",
                        new String[0]
                ),
                Arguments.of("Single Match",
                        new String[]{"batch_id","batch.total_seats","available_seats"},
                        "(batch\\.)",
                        new String[]{"batch.total_seats"}
                ),
                Arguments.of("Multiple Match",
                        new String[]{"xyxz.batch.batch_id","batch.total_seats","a.b.batch.c.d.batch.e.available_seats","fees"},
                        "(batch\\.)",
                        new String[]{"xyxz.batch.batch_id","batch.total_seats","a.b.batch.c.d.batch.e.available_seats"}
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void filterMatched_Exception(String testName, String[] src, String pattern, Class<Throwable> throwableClass) {
        assertThrows(throwableClass,() -> Helpers.filterMatched(src,pattern),testName);
    }

    private static Stream<Arguments> filterMatched_Exception() {
        return Stream.of(
                Arguments.of("Null src",
                        null,
                        "pattern",
                        IllegalArgumentException.class
                ),
                Arguments.of("0 Length src",
                        new String[0],
                        "pattern",
                        IllegalArgumentException.class
                ),
                Arguments.of("Null pattern",
                        new String[]{"item"},
                        null,
                        IllegalArgumentException.class
                ),
                Arguments.of("empty pattern",
                        new String[]{"item"},
                        "",
                        IllegalArgumentException.class
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void removeMatched(String testName, String[] src, String pattern, String[] expected) {
        String[] actual = Helpers.removeMatched(src,pattern);
        assertArrayEquals(expected,actual,testName);
    }

    private static Stream<Arguments> removeMatched() {
        return Stream.of(
                Arguments.of("No Match",
                        new String[]{"batch_id","total_seats","available_seats"},
                        "(batch\\.)",
                        new String[]{"batch_id","total_seats","available_seats"}
                ),
                Arguments.of("Single Match",
                        new String[]{"batch_id","batch.total_seats","available_seats"},
                        "(batch\\.)",
                        new String[]{"batch_id","available_seats"}
                ),
                Arguments.of("Multiple Match",
                        new String[]{"xyxz.batch.batch_id","batch.total_seats","a.b.batch.c.d.batch.e.available_seats","fees"},
                        "(batch\\.)",
                        new String[]{"fees"}
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void removeMatched_Exception(String testName, String[] src, String pattern, Class<Throwable> throwableClass) {
        assertThrows(throwableClass,() -> Helpers.removeMatched(src,pattern),testName);
    }

    private static Stream<Arguments> removeMatched_Exception() {
        return Stream.of(
                Arguments.of("Null src",
                        null,
                        "pattern",
                        IllegalArgumentException.class
                ),
                Arguments.of("0 Length src",
                        new String[0],
                        "pattern",
                        IllegalArgumentException.class
                ),
                Arguments.of("Null pattern",
                        new String[]{"item"},
                        null,
                        IllegalArgumentException.class
                ),
                Arguments.of("empty pattern",
                        new String[]{"item"},
                        "",
                        IllegalArgumentException.class
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void replaceEachMatched(String testName, String[] src, String patter, String with, String[] expected) {
        String[] actual = Helpers.replaceEachMatched(src,patter,with);
        assertArrayEquals(expected,actual,testName);
    }

    private static Stream<Arguments> replaceEachMatched() {
        return Stream.of(
                Arguments.of("No Match",
                        new String[]{"course_id","name","description"},
                        "(course\\.)",
                        "",
                        new String[]{"course_id","name","description"}
                ),
                Arguments.of("Single Match",
                        new String[]{"course_id","bach_id","course.description"},
                        "(course\\.)",
                        "",
                        new String[]{"course_id","bach_id","description"}
                ),
                Arguments.of("Multiple Match",
                        new String[]{"course.cghcg.course.course_id","course.fdfd.COURSE.name","description"},
                        "(course\\.)",
                        "",
                        new String[]{"cghcg.course_id","fdfd.COURSE.name","description"}
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void replaceEachMatched_exception(String testName, String[] src, String patter, String with, Class<Throwable> throwableClass) {
        assertThrows(throwableClass,() -> Helpers.replaceEachMatched(src,patter,with),testName);
    }

    private static Stream<Arguments> replaceEachMatched_exception() {
        return Stream.of(
                Arguments.of("Null src",
                        null,
                        "pattern",
                        "with",
                        IllegalArgumentException.class
                ),
                Arguments.of("0 Length src",
                        new String[0],
                        "pattern",
                        "with",
                        IllegalArgumentException.class
                ),
                Arguments.of("Null pattern",
                        new String[]{"item"},
                        null,
                        "with",
                        IllegalArgumentException.class
                ),
                Arguments.of("empty pattern",
                        new String[]{"item"},
                        "",
                        "with",
                        IllegalArgumentException.class
                ),
                Arguments.of("null with",
                        new String[]{"item"},
                        "pattern",
                        null,
                        NullPointerException.class
                )
        );
    }
}