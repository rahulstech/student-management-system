package rahulstech.javafx.studentmanagementsystem.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import rahulstech.javafx.studentmanagementsystem.model.Sex;
import rahulstech.javafx.studentmanagementsystem.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isContentEqual;

class UserDaoTest extends BaseDBTest {

    @Test
    void createUser() {
        User user = newUser(null,"user3","pass3",
                "ugn3","ufn3","OTHER","u_photo3","u_phone3");
        assertDoesNotThrow(() -> getDb().getUserDao().createUser(user),
                "createUser throws exception for valid values");
    }

    @ParameterizedTest
    @MethodSource
    void createUser_Exception(String testName, User user, Class<Throwable> expected) {
        assertThrows(expected,() -> getDb().getUserDao().createUser(user),testName);
    }

    private static Stream<Arguments> createUser_Exception() {
        return Stream.of(
                Arguments.of("Null Values For NonNull Fields",new User(),DatabaseException.class),
                Arguments.of("Not Unique Username",newUser(null,"user1","pass3",
                        "ugn3","ufn3","OTHER","u_photo3","u_phone3"),
                        DatabaseException.class)
        );
    }



    @ParameterizedTest
    @MethodSource
    void login(String testName, String username, String password, User expected) {
        User actual = getDb().getUserDao().login(username,password);
        assertTrue(isContentEqual(expected,actual),testName);
    }

    private static Stream<Arguments> login() {
        return Stream.of(
                Arguments.of("Existing User","user1","pass1",
                        newUser("U1","user1","pass1",
                                "ugn1","ufn1","MALE",
                                "u_photo1","u_phone1")),
                Arguments.of("No Match Username","user24","pass1",
                        null)
        );
    }

    @Test
    void getAllUsers() {
        List<User> expected = Arrays.asList(
                newUser("U1","user1","pass1",
                        "ugn1","ufn1","MALE","u_photo1","u_phone1"),
                newUser("U2","user2","pass2",
                        "ugn2","ufn2","FEMALE","u_photo2","u_phone2")
        );
        List<User> actual = getDb().getUserDao().getAllUsers();
        assertNotNull(actual,"getAllUser returned null");
        assertEquals(expected.size(),actual.size(),"getAllUsers: different size");
        for (int i = 0; i < expected.size(); i++) {
            User ei = expected.get(i);
            User ai = actual.get(i);
            assertTrue(isContentEqual(ei,ai),"getAllUsers: different content");
        }
    }

    private static User newUser(String user_id,String username, String password,
                                String given_name,String family_name,
                                String sex,String photo_url,String phone) {
        User user = new User();
        user.setUserId(user_id);
        user.setUsername(username);
        user.setPassword(password);
        user.setGivenName(given_name);
        user.setFamilyName(family_name);
        user.setSex(null == sex ? null : Sex.from(sex));
        user.setPhotoURL(photo_url);
        user.setPhone(phone);
        return user;
    }
}