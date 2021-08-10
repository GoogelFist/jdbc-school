package ua.com.foxminded.school.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.dao.StudentsCourseDao;
import ua.com.foxminded.school.dao.entity.Group;
import ua.com.foxminded.school.dao.entity.Student;
import ua.com.foxminded.school.ui.Manager;

import java.util.List;

public class Facade {

    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final StudentsCourseDao studentsCoursesDao;
    private final Generator dataGenerator;
    private final Creator tableCreator;
    private final Manager uiManager;
    private static final String CREATE_TABLES_SCRIPT_PATH = "src/main/resources/scripts/CreateTables.sql";
    private static final Logger logger = LoggerFactory.getLogger(Facade.class);

    public Facade(CourseDao courseDao, GroupDao groupDao, StudentDao studentDao, StudentsCourseDao studentsCoursesDao,
                  Generator dataGenerator, Creator tableCreator, Manager uiManager) {
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.studentsCoursesDao = studentsCoursesDao;
        this.dataGenerator = dataGenerator;
        this.tableCreator = tableCreator;
        this.uiManager = uiManager;
    }


    public void runApplication() {
        tableCreator.createTables(CREATE_TABLES_SCRIPT_PATH);
        logger.info("Tables was created");

        List<Group> groups = dataGenerator.generateGroups();
        groups.forEach(groupDao::create);
        logger.info("Groups data was generated and added to base");

        dataGenerator.generateCourses().forEach(courseDao::create);
        logger.info("Courses data was generated and added to base");

        List<Student> students = dataGenerator.generateStudents();
        logger.info("Students data was generated");
        dataGenerator.randomAssignStudentsToGroups(students, groups);
        logger.info("Assign students to to groups");

        students.forEach(studentDao::create);
        logger.info("Students data was added to base");

        dataGenerator.generateStudentsCourses(students).forEach(studentsCoursesDao::create);
        logger.info("StudentsCourses data was generated and added to base");

        uiManager.runApplicationMenu();
    }
}