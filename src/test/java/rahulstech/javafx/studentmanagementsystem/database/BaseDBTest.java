package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.InputStreamReader;
import java.io.Reader;

public class BaseDBTest {

    private StudentDB db;
    private ScriptRunner runner;

    @BeforeEach
    void setUp() throws Exception {
        db = new StudentDB();
        runner = new ScriptRunner(db.getConnection(),false,false);
        Reader before = new InputStreamReader(ClassLoader.getSystemResourceAsStream("script_before.sql"));
        runner.runScript(before);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (null != db) {
            db.close();
        }
    }

    public StudentDB getDb() {
        return db;
    }
}
