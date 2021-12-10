package com.sprobotics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileEditModel {

    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public class Details {

        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("customer_email")
        @Expose
        private String customerEmail;
        @SerializedName("customer_contact_no")
        @Expose
        private String customerContactNo;
        @SerializedName("student_name")
        @Expose
        private String studentName;
        @SerializedName("student_email_id")
        @Expose
        private String studentEmailId;
        @SerializedName("student_contact_no")
        @Expose
        private String studentContactNo;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("gender")
        @Expose
        private String gender;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        public String getCustomerContactNo() {
            return customerContactNo;
        }

        public void setCustomerContactNo(String customerContactNo) {
            this.customerContactNo = customerContactNo;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getStudentEmailId() {
            return studentEmailId;
        }

        public void setStudentEmailId(String studentEmailId) {
            this.studentEmailId = studentEmailId;
        }

        public String getStudentContactNo() {
            return studentContactNo;
        }

        public void setStudentContactNo(String studentContactNo) {
            this.studentContactNo = studentContactNo;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class Datum {

        @SerializedName("details")
        @Expose
        private Details details;

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details details) {
            this.details = details;
        }

    }

}
