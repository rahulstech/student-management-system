package rahulstech.javafx.studentmanagementsystem.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rahulstech.javafx.studentmanagementsystem.model.Sex;
import rahulstech.javafx.studentmanagementsystem.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static rahulstech.javafx.studentmanagementsystem.util.Helpers.hash;

public class UserDao {

    private static final Logger logger = LogManager.getLogger(UserDao.class);

    private StudentDB db;

    UserDao(StudentDB db) {
        this.db = db;
    }

    public User createUser(User user) {
        User copy = user.clone();
        copy.setUserId(generateNewUserId());
        String sql = "INSERT INTO users (user_id,username,password,given_name,family_name,sex,photo_url,phone)"+
                " VALUES (?,?,?,?,?,?,?,?);";
        logger.debug("sql: "+sql+" | values: ["+copy+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,copy.getUserId());
            ps.setString(2,copy.getUsername());
            ps.setString(3,copy.getPassword());
            ps.setString(4,copy.getGivenName());
            ps.setString(5,copy.getFamilyName());
            ps.setString(6,copy.getSex().getValue());
            ps.setString(7,copy.getPhotoURL());
            ps.setString(8,copy.getPhone());
            ps.execute();
            return copy;
        }
        catch (Exception e) {
            logger.error("createUser",e);
            throw new DatabaseException(e);
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?;";
        logger.debug("sql: "+sql+" | values: [username=\""+username+"\", password=\""+password+"\"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if (null != rs && rs.next()) {
                User user = newUser(rs);
                return user;
            }
            return null;
        }
        catch (Exception e) {
            logger.error("login",e);
            throw new DatabaseException(e);
        }
    }

    public List<User> getAllUsers() {
        Connection conn = db.getConnection();
        try (Statement s = conn.createStatement()) {
            ResultSet rs = s.executeQuery("SELECT * FROM users;");
            if (null != rs) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    User user = newUser(rs);
                    users.add(user);
                }
                return users;
            }
            return Collections.emptyList();
        }
        catch (Exception e) {
            logger.error("getAllUsers",e);
            throw new DatabaseException(e);
        }
    }

    private String generateNewUserId() {
        return UUID.randomUUID().toString();
    }

    private User newUser(ResultSet rs) throws Exception {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setGivenName(rs.getString("given_name"));
        user.setFamilyName(rs.getString("family_name"));
        user.setSex(Sex.from(rs.getString("sex")));
        user.setPhotoURL(rs.getString("photo_url"));
        user.setPhone(rs.getString("phone"));
        return user;
    }
}
