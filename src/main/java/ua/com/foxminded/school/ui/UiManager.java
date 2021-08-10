package ua.com.foxminded.school.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.school.dao.*;
import ua.com.foxminded.school.dao.entity.Course;
import ua.com.foxminded.school.dao.entity.Student;
import ua.com.foxminded.school.dao.entity.StudentCourse;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.*;

public class UiManager implements Manager {
    private static final Logger logger = LoggerFactory.getLogger(UiManager.class);

    private static final String Q_IN_QUOTES = "\"q\"";
    private static final String ADD_NEW_STUDENT_GROUP_ID_MESSAGE = "Input group id for student:";
    private static final String ADD_NEW_STUDENT_FIRST_NAME_MESSAGE = "Input first name for student:";
    private static final String ADD_NEW_STUDENT_LAST_NAME_MESSAGE = "Input last name for student:";
    private static final String FIND_GROUP_WITH_STUDENT_COUNT_OUT_FORMAT = "Group %s : %d students" + lineSeparator();
    private static final String INPUT_COURSE_NAME_MESSAGE = "Input course name:";
    private static final String INPUT_STUDENTS_COUNT_MESSAGE = "Input student count:";
    private static final String INPUT_STUDENT_ID_MESSAGE = "Input student id:";
    private static final String CHOOSE_ID_OF_COURSE_MESSAGE = "Choose course id for add student";
    private static final String STRING_FORMAT_FOR_OUT_COURSES = "Course id - %d, course name - %s" + lineSeparator();
    private static final String STRING_FORMAT_OUT_COURSES_OF_STUDENT = "Student have courses: course id - %d, course name - %s" + lineSeparator();
    private static final String MESSAGE_FOR_DELETE_STUDENT_FROM_COURSE = "Insert course id for delete it";

    private final Connector connector;

    public UiManager(Connector connector) {
        this.connector = connector;
    }

    public void runApplicationMenu() {
        Scanner scanner = new Scanner(in);
        showMenuOptions();
        String answer = scanner.nextLine();
        while (!answer.equals("q")) {
            switch (answer) {
                case "a":
                    findGroupsWithLessOrEqualsStudentCount();
                    showMenuOptions();
                    answer = scanner.nextLine();
                    break;
                case "b":
                    findAllStudentsRelatedToCourse();
                    showMenuOptions();
                    answer = scanner.nextLine();
                    break;
                case "c":
                    addNewStudent();
                    showMenuOptions();
                    answer = scanner.nextLine();
                    break;
                case "d":
                    deleteStudentById();
                    showMenuOptions();
                    answer = scanner.nextLine();
                    break;
                case "e":
                    addStudentToTheCourseFromList();
                    showMenuOptions();
                    answer = scanner.nextLine();
                    break;
                case "f":
                    removeTheStudentFromCourse();
                    showMenuOptions();
                    answer = scanner.nextLine();
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + answer);
            }
        }
    }

    private void showMenuOptions() {
        String menuBuilder = "a. Find all groups with less or equals student count" + lineSeparator() +
            "b. Find all students related to course with given name" + lineSeparator() +
            "c. Add new student" + lineSeparator() +
            "d. Delete student by STUDENT_ID" + lineSeparator() +
            "e. Add a student to the course (from a list)" + lineSeparator() +
            "f. Remove the student from one of his or her courses" + lineSeparator() +
            "Press " + Q_IN_QUOTES + " for exit.";
        out.println(menuBuilder);
    }

    private void findGroupsWithLessOrEqualsStudentCount() {
        Scanner scanner = new Scanner(in);
        out.println(INPUT_STUDENTS_COUNT_MESSAGE);
        int expectCount = scanner.nextInt();
        GroupDao groupDao = new GroupDaoImpl(connector);
        Map<String, Integer> groups = groupDao.getByStudentsCount(expectCount);
        groups.forEach((g, c) -> out.printf(FIND_GROUP_WITH_STUDENT_COUNT_OUT_FORMAT, g, c));
        out.println();
    }

    private void findAllStudentsRelatedToCourse() {
        Scanner scanner = new Scanner(in);
        out.println(INPUT_COURSE_NAME_MESSAGE);
        String expectName = scanner.next();
        StudentsCourseDao studentsCoursesDao = new StudentsCourseDaoImpl(connector);
        List<String> studentsRelatedToCourse = studentsCoursesDao.getStudentsRelatedToCourse(expectName);
        studentsRelatedToCourse.forEach(out::println);
        out.println();
    }

    private void addNewStudent() {
        Scanner scanner = new Scanner(in);
        out.println(ADD_NEW_STUDENT_GROUP_ID_MESSAGE);
        int groupId = scanner.nextInt();
        out.println(ADD_NEW_STUDENT_FIRST_NAME_MESSAGE);
        String firstName = scanner.nextLine();
        out.println(ADD_NEW_STUDENT_LAST_NAME_MESSAGE);
        String lastName = scanner.nextLine();
        StudentDao studentDao = new StudentDaoImpl(connector);
        studentDao.create(new Student(groupId, firstName, lastName));
        logger.info("Student was created");
        out.println();
    }

    private void deleteStudentById() {
        Scanner scanner = new Scanner(in);
        out.println(INPUT_STUDENT_ID_MESSAGE);
        int studentId = scanner.nextInt();
        StudentDao studentDao = new StudentDaoImpl(connector);
        studentDao.deleteById(studentId);
        logger.info("Student was deleted");
        out.println();
    }

    private void addStudentToTheCourseFromList() {
        Scanner scanner = new Scanner(in);
        out.println(INPUT_STUDENT_ID_MESSAGE);
        int studentId = scanner.nextInt();
        CourseDao courseDao = new CourseDaoImpl(connector);
        List<Course> allCourses = courseDao.getAll();
        allCourses.forEach(course -> out.printf(STRING_FORMAT_FOR_OUT_COURSES, course.getId(), course.getName()));
        out.println();
        out.println(CHOOSE_ID_OF_COURSE_MESSAGE);
        out.println();
        int courseId = scanner.nextInt();
        StudentsCourseDao studentsCourseDao = new StudentsCourseDaoImpl(connector);
        studentsCourseDao.create(new StudentCourse(studentId, courseId));
        logger.info("Student was added to course");
        out.println();
    }

    private void removeTheStudentFromCourse() {
        Scanner scanner = new Scanner(in);
        out.println(INPUT_STUDENT_ID_MESSAGE);
        int studentId = scanner.nextInt();
        StudentsCourseDao studentsCourseDao = new StudentsCourseDaoImpl(connector);
        Map<Integer, String> coursesByStudentId = studentsCourseDao.getCoursesByStudentId(studentId);
        coursesByStudentId.forEach((courseId, courseName) ->
            out.printf(STRING_FORMAT_OUT_COURSES_OF_STUDENT, courseId, courseName));
        out.println();
        out.println(MESSAGE_FOR_DELETE_STUDENT_FROM_COURSE);
        int courseIdForDelete = scanner.nextInt();
        studentsCourseDao.deleteCourseById(studentId, courseIdForDelete);
        logger.info("Course was deleted");
        out.println();
    }
}