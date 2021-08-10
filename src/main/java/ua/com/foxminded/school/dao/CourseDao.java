package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.entity.Course;

import java.util.List;

public interface CourseDao {
    void create(Course course);

    List<Course> getAll();
}