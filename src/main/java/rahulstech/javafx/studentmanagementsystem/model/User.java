package rahulstech.javafx.studentmanagementsystem.model;

import rahulstech.javafx.studentmanagementsystem.util.EqualsContent;

import java.util.Objects;

public class User implements Cloneable, EqualsContent {

    private String userId;
    private String username;
    private String password;
    private String givenName;
    private String familyName;
    private Sex sex;
    private String photoURL;
    private String phone;

    public User() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", sex=" + sex +
                ", photoURL='" + photoURL + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    public boolean equalsContent(Object o) {
        if (null == o) return false;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(givenName, user.givenName) &&
                Objects.equals(familyName, user.familyName) &&
                sex == user.sex &&
                Objects.equals(photoURL, user.photoURL) &&
                Objects.equals(phone, user.phone);
    }

    public User clone() {
        User copy = new User();
        copy.userId = this.userId;
        copy.username = this.username;
        copy.password = this.password;
        copy.givenName = this.givenName;
        copy.familyName = this.familyName;
        copy.sex = this.sex;
        copy.photoURL = this.photoURL;
        copy.phone = this.phone;
        return copy;
    }
}
