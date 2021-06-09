package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDBTest extends BaseDBTest {

    @ParameterizedTest
    @MethodSource
    void generateAndGetId(String testName, String tableName, String pattern, String expected) {
        String newCourseID = getDb().generateAndGetId(tableName, pattern);
        assertEquals(expected,newCourseID,testName);
    }

    private static Stream<Arguments> generateAndGetId() {
        return Stream.of(
                Arguments.of("Generate Id For Existing Pattern","some_table","ST2021","ST20212"),
                Arguments.of("Generate Id For Non-Existing Pattern","another_table","AT2021","AT20211")
        );
    }
}