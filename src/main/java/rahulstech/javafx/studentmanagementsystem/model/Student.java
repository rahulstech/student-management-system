package rahulstech.javafx.studentmanagementsystem.model;

import rahulstech.javafx.studentmanagementsystem.util.EqualsContent;

import java.time.LocalDate;
import java.util.Objects;

public class Student implements EqualsContent {

    private String studentId;
    private String givenName;
    private String familyName;
    private Sex sex;
    private String photoURL;
    private LocalDate dateOfBirth;
    private String address;
    private String phone;
    private String email;
    private LocalDate dateOfJoin;

    public Student() {}

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(LocalDate dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", sex=" + sex +
                ", photoURL='" + photoURL + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", dateOfJoin=" + dateOfJoin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    public boolean equalsContent(Object o) {
        if (null == o) return false;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId) &&
                Objects.equals(givenName, student.givenName) &&
                Objects.equals(familyName, student.familyName) &&
                sex == student.sex &&
                Objects.equals(photoURL, student.photoURL) &&
                Objects.equals(dateOfBirth, student.dateOfBirth) &&
                Objects.equals(address, student.address) &&
                Objects.equals(phone, student.phone) &&
                Objects.equals(email, student.email) &&
                Objects.equals(dateOfJoin, student.dateOfJoin);
    }

    public Student clone() {
        Student copy = new Student();
        copy.studentId = this.studentId;
        copy.givenName = this.givenName;
        copy.familyName = this.familyName;
        copy.sex = this.sex;
        copy.photoURL = this.photoURL;
        copy.dateOfBirth = this.dateOfBirth;
        copy.address = this.address;
        copy.phone = this.phone;
        copy.email = this.email;
        copy.dateOfJoin = this.dateOfJoin;
        return copy;
    }
}
