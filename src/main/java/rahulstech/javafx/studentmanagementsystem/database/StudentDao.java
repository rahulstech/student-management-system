package rahulstech.javafx.studentmanagementsystem.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rahulstech.javafx.studentmanagementsystem.model.Sex;
import rahulstech.javafx.studentmanagementsystem.model.Student;

import java.sql.*;
import java.time.LocalDate;

import static rahulstech.javafx.studentmanagementsystem.util.Helpers.arrayToString;
import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isEmptyArray;

public class StudentDao {

    private static final Logger logger = LogManager.getLogger(StudentDao.class);

    private StudentDB db;

    StudentDao(StudentDB db) {
        this.db = db;
    }

    public Student addStudent(Student student) {
        String sql = "INSERT INTO students (student_id,given_name,family_name,sex,photo_url,date_of_birth," +
                "address,phone,email,date_of_join) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?);";
        Student copy = student.clone();
        copy.setStudentId(generateNewStudentId());
        logger.debug("sql: "+sql+" | values: ["+copy+"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,copy.getStudentId());
            ps.setString(2,copy.getGivenName());
            ps.setString(3,copy.getFamilyName());
            ps.setString(4,copy.getSex().getValue());
            ps.setString(5,copy.getPhotoURL());
            ps.setDate(6, Date.valueOf(copy.getDateOfBirth()));
            ps.setString(7,copy.getAddress());
            ps.setString(8,copy.getPhone());
            ps.setString(9,copy.getPhone());
            ps.setDate(10,Date.valueOf(copy.getDateOfJoin()));
            ps.execute();
            return copy;
        }
        catch (Exception e) {
            logger.error("addStudent",e);
            throw new DatabaseException(e);
        }
    }

    public Student getStudentByStudentId(String studentId, String[] columns) {
        String sql = "SELECT ";
        sql += null == columns || 0 == columns.length ? "*" : arrayToString(columns,",");
        sql += " FROM students WHERE student_id = ?;";
        logger.debug("sql: "+sql+" | values: [student_id=\""+studentId+"\"]");
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,studentId);
            ResultSet rs = ps.executeQuery();
            if (null != rs && rs.next()) {
                Student student = isEmptyArray(columns) ? newStudent(rs) : newStudent(rs,columns);
                return student;
            }
            return null;
        }
        catch (Exception e) {
            logger.error("addStudent",e);
            throw new DatabaseException(e);
        }
    }

    public boolean updateStudent(Student student) {
        Connection conn = db.getConnection();
        String sql = "UPDATE students SET given_name = ?, family_name = ?, sex = ?, date_of_birth = ?, photo_url = ?," +
                " address = ?, phone = ?, email = ? WHERE student_id = ?;";
        logger.debug("sql: "+sql+" | values: ["+student+"]");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,student.getGivenName());
            ps.setString(2,student.getFamilyName());
            ps.setString(3,student.getSex().getValue());
            ps.setDate(4,Date.valueOf(student.getDateOfBirth()));
            ps.setString(5,student.getPhotoURL());
            ps.setString(6,student.getAddress());
            ps.setString(7,student.getPhone());
            ps.setString(8,student.getEmail());
            ps.setString(9,student.getStudentId());
            return ps.executeUpdate() > 0;
        }
        catch (Exception e) {
            logger.error("updateStudent",e);
            throw new DatabaseException(e);
        }
    }

    private String generateNewStudentId() {
        String tableName = "students";
        String pattern = "STUD"+LocalDate.now().getYear();
        return db.generateAndGetId(tableName,pattern);
    }

    private Student newStudent(ResultSet rs) throws Exception {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setGivenName(rs.getString("given_name"));
        student.setFamilyName(rs.getString("family_name"));
        student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        student.setSex(Sex.from(rs.getString("sex")));
        student.setPhotoURL(rs.getString("photo_url"));
        student.setAddress(rs.getString("address"));
        student.setPhone(rs.getString("phone"));
        student.setEmail(rs.getString("email"));
        student.setDateOfJoin(rs.getDate("date_of_join").toLocalDate());
        return student;
    }

    private Student newStudent(ResultSet rs, String[] columns) throws Exception {
        Student student = new Student();
        for (String col : columns) {
            switch (col) {
                case "student_id": student.setStudentId(rs.getString("student_id"));
                break;
                case "given_name": student.setGivenName(rs.getString("given_name"));
                break;
                case "family_name": student.setFamilyName(rs.getString("family_name"));
                break;
                case "date_of_birth": student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                break;
                case "sex": student.setSex(Sex.from(rs.getString("sex")));
                break;
                case "photo_url": student.setPhotoURL(rs.getString("photo_url"));
                break;
                case "address": student.setAddress(rs.getString("address"));
                break;
                case "phone": student.setPhone(rs.getString("phone"));
                break;
                case "email": student.setEmail(rs.getString("email"));
                break;
                case "date_of_join": student.setDateOfJoin(rs.getDate("date_of_join").toLocalDate());
                break;
            }
        }
        return student;
    }
}
