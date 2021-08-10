package ua.com.foxminded.school.domain;

import ua.com.foxminded.school.dao.entity.Course;
import ua.com.foxminded.school.dao.entity.Group;
import ua.com.foxminded.school.dao.entity.Student;
import ua.com.foxminded.school.dao.entity.StudentCourse;

import java.util.List;

public interface Generator {
    List<Group> generateGroups();

    List<StudentCourse> generateStudentsCourses(List<Student> students);

    List<Course> generateCourses();

    List<Student> generateStudents();

    void randomAssignStudentsToGroups(List<Student> students, List<Group> groups);
}
