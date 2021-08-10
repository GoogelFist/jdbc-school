package ua.com.foxminded.school.dao.entity;

import java.util.Objects;

public class Student {
    private int id;
    private int groupId;
    private String fistName;
    private String lastName;

    public Student(int id, int groupId, String fistName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.fistName = fistName;
        this.lastName = lastName;
    }

    public Student(int groupId, String fistName, String lastName) {
        this.groupId = groupId;
        this.fistName = fistName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && groupId == student.groupId && Objects.equals(fistName, student.fistName) && Objects.equals(lastName, student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, fistName, lastName);
    }
}