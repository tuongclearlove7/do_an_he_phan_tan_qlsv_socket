package org.example.server.DAL.repository;

import org.example.server.DAL.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public StudentRepository() {
    }

    public List<Student> studentStaticRepositories(){
        List<Student> studentList =new ArrayList<>();
        studentList.add(new Student("SV001", "Nguyen Van A", "16-01-2002", "SV001"));
        studentList.add(new Student("SV002", "Nguyen Van B", "16-01-2001", "SV002"));
        studentList.add(new Student("SV003", "Nguyen Van K", "16-01-2003", "SV003"));
        studentList.add(new Student("SV004", "Nguyen Van C", "16-01-2005", "SV004"));
        return studentList;
    }
}
