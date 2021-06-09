package rahulstech.javafx.studentmanagementsystem.model;

import rahulstech.javafx.studentmanagementsystem.util.EqualsContent;

import java.time.LocalDate;
import java.util.Objects;

import static rahulstech.javafx.studentmanagementsystem.util.Helpers.isContentEqual;

public class Admission implements EqualsContent {

    private String admissionId;
    private Student student;
    private Course course;
    private LocalDate admissionDate;
    private AdmissionStatus status;
    private Float netPayable;
    private Float duePayment;

    public Admission() {}

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public AdmissionStatus getStatus() {
        return status;
    }

    public void setStatus(AdmissionStatus status) {
        this.status = status;
    }

    public Float getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(Float netPayable) {
        this.netPayable = netPayable;
    }

    public Float getDuePayment() {
        return duePayment;
    }

    public void setDuePayment(Float duePayment) {
        this.duePayment = duePayment;
    }

    @Override
    public String toString() {
        return "Admission{" +
                "admissionId='" + admissionId + '\'' +
                ", student=" + student +
                ", course=" + course +
                ", admissionDate=" + admissionDate +
                ", status=" + status +
                ", netPayable=" + netPayable +
                ", duePayment=" + duePayment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admission)) return false;
        Admission admission = (Admission) o;
        return Objects.equals(admissionId, admission.admissionId);
    }

    public boolean equalsContent(Object o) {
        if (o == null) return false;
        if (!(o instanceof Admission)) return false;
        Admission admission = (Admission) o;
        return Objects.equals(admissionId, admission.admissionId) &&
                isContentEqual(student,admission.student) &&
                isContentEqual(course, admission.course) &&
                Objects.equals(admissionDate, admission.admissionDate) &&
                status == admission.status &&
                Objects.equals(netPayable, admission.netPayable) &&
                Objects.equals(duePayment, admission.duePayment);
    }

    public Admission clone() {
        Admission copy = new Admission();
        copy.admissionId = this.admissionId;
        copy.student = null == this.status ? null : this.student.clone();
        copy.course = null == this.course ? null : this.course.clone();
        copy.admissionDate = this.admissionDate;
        copy.status = this.status;
        copy.netPayable = this.netPayable;
        copy.duePayment = this.duePayment;
        return copy;
    }
}
