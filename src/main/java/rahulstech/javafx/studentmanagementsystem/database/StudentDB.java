package rahulstech.javafx.studentmanagementsystem.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

import static rahulstech.javafx.studentmanagementsystem.util.ResourceLoader.loadProperties;

public class StudentDB {

    private static final Logger logger = LogManager.getLogger(StudentDB.class);

    private static StudentDB mInstance;

    private DataSource dataSource;

    private UserDao userDao = null;
    private StudentDao studentDao = null;
    private CourseDao courseDao = null;
    private AdmissionDao admissionDao = null;
    private ScheduleDao scheduleDao = null;
    private PaymentDao paymentDao = null;


    StudentDB() {
        init();
    }

    private void init() {
        Properties conf = loadProperties("database.properties");
        MysqlDataSource mysql = new MysqlDataSource();
        mysql.setServerName(conf.getProperty("host"));
        mysql.setPort(Integer.valueOf(conf.getProperty("port","3306")));
        mysql.setDatabaseName(conf.getProperty("database_name"));
        mysql.setUser(conf.getProperty("username"));
        mysql.setPassword(conf.getProperty("password"));
        this.dataSource = mysql;
    }

    public static StudentDB getInstance() {
        if (null == mInstance) {
            mInstance = new StudentDB();
        }
        return mInstance;
    }

    Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public boolean setAutoCommit(Connection conn, boolean autoCommit) {
        boolean oldValue = true;
        try {
            oldValue = conn.getAutoCommit();
            if (oldValue != autoCommit) {
                conn.setAutoCommit(autoCommit);
            }
            return true;
        }
        catch (Exception e) {
            logger.error("setAutoCommit",e);
            return false;
        }
        finally {
            try {
                conn.setAutoCommit(oldValue);
            } catch (Exception e) {
                logger.error("setAutoCommit -> finally",e);
            }
        }
    }

    public void commit(Connection conn) {
        try {
            conn.commit();
        }
        catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void rollback(Connection conn) {
        try {
            conn.rollback();
        }
        catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void closeStatement(Statement statement) {
        try {
            statement.close();
        }
        catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    public void closeResultSet(ResultSet resultSet) {
        try {
            resultSet.close();
        }
        catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    public void close() {
        try {
            DataSource dataSource = this.dataSource;
            Connection conn = dataSource.getConnection();
            conn.close();
        }
        catch (Exception e) {
            logger.error("close",e);
        }
        finally {
            this.dataSource = null;
            mInstance = null;
        }
    }

    public String generateAndGetId(String tableName, String pattern) {
        Connection conn = getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        try {
                String insert = "INSERT INTO sequences (table_name,pattern,create_count) VALUES (\""+tableName+"\",\""+pattern+"\",1) " +
                        "ON DUPLICATE KEY UPDATE create_count = create_count + 1;";
                logger.debug("sql: "+insert+"| values: [tableName=\""+tableName+"\", pattern=\""+pattern+"\"]");
                stmt = conn.createStatement();
                stmt.execute(insert);

                String query = "SELECT create_count FROM sequences WHERE table_name = \""+tableName+"\" AND pattern = \""+pattern+"\";";
                logger.debug("sql: "+query+"| values: [tableName=\""+tableName+"\", pattern=\""+pattern+"\"]");
                rs = conn.createStatement().executeQuery(query);
                if (null != rs && rs.next()) {
                    int createCount = rs.getInt("create_count");
                    String newId = pattern+createCount;
                    logger.debug("newId: "+newId);
                    return newId;
                }
                throw new DatabaseException("unable to generate new id for table=\""+tableName+"'\" with patter=\""+pattern+"\"");
        }
        catch (DatabaseException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DatabaseException(e);
        }
        finally {
            closeStatement(stmt);
            closeResultSet(rs);
        }
    }

    public UserDao getUserDao() {
        if (null == userDao) {
            this.userDao = new UserDao(this);
        }
        return userDao;
    }

    public StudentDao getStudentDao() {
        if (null == studentDao) {
            this.studentDao = new StudentDao(this);
        }
        return studentDao;
    }

    public CourseDao getCourseDao() {
        if (null == courseDao) {
            this.courseDao = new CourseDao(this);
        }
        return courseDao;
    }

    public ScheduleDao getScheduleDao() {
        if (null == scheduleDao) {
            this.scheduleDao = new ScheduleDao(this);
        }
        return scheduleDao;
    }

    public AdmissionDao getAdmissionDao() {
        if (null == admissionDao) {
            this.admissionDao = new AdmissionDao(this);
        }
        return admissionDao;
    }

    public PaymentDao getPaymentDao() {
        if (null == paymentDao) {
            paymentDao = new PaymentDao(this);
        }
        return paymentDao;
    }
}
