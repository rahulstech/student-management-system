package rahulstech.javafx.studentmanagementsystem.util;

import java.util.Properties;

public class ResourceLoader {

    public static Properties loadProperties(String fileName) {
        try {
            Properties properties = new Properties();
            properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream(fileName));
            return properties;
        }
        catch (Exception e) {
            throw new ResourceLoadException(e);
        }
    }
}
