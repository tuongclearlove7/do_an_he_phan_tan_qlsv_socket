package org.example.server.DAL.model;

public class Student {

    String id;
    String fullname;
    String birthday;
    String code;

    public Student(String id, String fullname, String birthday, String code) {
        this.id = id;
        this.fullname = fullname;
        this.birthday = birthday;
        this.code = code;
    }

    public Student(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "\nID: " + this.id + ", Fullname: " + this.fullname + ", Birthday: " + this.birthday + ", Code: " + this.code + "\n";
    }

}
