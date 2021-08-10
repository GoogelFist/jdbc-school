package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.dao.entity.StudentCourse;

import java.util.List;
import java.util.Map;

public interface StudentsCourseDao {
    void create(StudentCourse studentCourse);

    List<String> getStudentsRelatedToCourse(String courseName);

    Map<Integer, String> getCoursesByStudentId(int id);

    void deleteCourseById(int studentId, int courseId);
}