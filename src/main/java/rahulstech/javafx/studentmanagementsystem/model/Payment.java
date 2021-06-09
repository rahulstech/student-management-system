package rahulstech.javafx.studentmanagementsystem.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {

    private String paymentId;
    private Admission admission;
    private Student student;
    private Course course;
    private Float amount;
    private LocalDateTime when;

    public Payment() {}

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Admission getAdmission() {
        return admission;
    }

    public void setAdmission(Admission admission) {
        this.admission = admission;
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", admission=" + admission +
                ", student=" + student +
                ", course=" + course +
                ", amount=" + amount +
                ", when=" + when +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId);
    }

    public boolean equalsContent(Payment payment) {
        if (null == payment) return false;
        return Objects.equals(paymentId, payment.paymentId) &&
                Objects.equals(admission, payment.admission) &&
                Objects.equals(student, payment.student) &&
                Objects.equals(course, payment.course) &&
                Objects.equals(amount, payment.amount) &&
                Objects.equals(when, payment.when);
    }

    public Payment clone() {
        Payment copy = new Payment();
        copy.paymentId = this.paymentId;
        copy.admission = null == this.admission ? null : this.admission.clone();
        copy.student = null == this.student ? null : this.student.clone();
        copy.course = null == this.course ? null : this.course.clone();
        copy.amount = this.amount;
        copy.when = this.when;
        return copy;
    }
}
