package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.entity.Student;

public interface StudentDao {
    void create(Student student);

    void deleteById(int studentId);
}