package ua.com.foxminded.school;

import ua.com.foxminded.school.dao.*;
import ua.com.foxminded.school.domain.*;
import ua.com.foxminded.school.ui.Manager;
import ua.com.foxminded.school.ui.UiManager;

public class Application {
    public static void main(String[] args) {
        Connector connector = new DbConnector("database");
        GroupDao groupDao = new GroupDaoImpl(connector);
        StudentDao studentDao = new StudentDaoImpl(connector);
        CourseDao courseDao = new CourseDaoImpl(connector);
        StudentsCourseDao studentsCourseDao = new StudentsCourseDaoImpl(connector);
        Generator generator = new DataGenerator();
        Creator tableCreator = new TableCreator(connector);
        Manager uiManager = new UiManager(connector);

        Facade facade = new Facade(courseDao, groupDao, studentDao, studentsCourseDao,
            generator, tableCreator, uiManager);
        facade.runApplication();
    }
}