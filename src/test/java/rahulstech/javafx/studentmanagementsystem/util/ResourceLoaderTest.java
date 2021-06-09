package rahulstech.javafx.studentmanagementsystem.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceLoaderTest {

    @Test
    void loadProperties() {
        assertDoesNotThrow(() -> {
            //ResourceLoader.loadProperties("conf/database.properties");
            ClassLoader.getSystemClassLoader().getResourceAsStream("conf/database.properties");
        });
    }
}