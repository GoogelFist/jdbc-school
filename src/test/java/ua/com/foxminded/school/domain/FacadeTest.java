package ua.com.foxminded.school.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.foxminded.school.dao.*;
import ua.com.foxminded.school.dao.entity.Course;
import ua.com.foxminded.school.dao.entity.Group;
import ua.com.foxminded.school.dao.entity.Student;
import ua.com.foxminded.school.dao.entity.StudentCourse;
import ua.com.foxminded.school.ui.Manager;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FacadeTest {
    @Mock
    private Connector mockConnector;
    @Mock
    private Connection mockConnection;
    @Mock
    private Creator mockTableCreator;
    @Mock
    private GroupDao mockGroupDao;
    @Mock
    private Generator mockGenerator;
    @Mock
    private StudentDao mockStudentDao;
    @Mock
    private CourseDao mockCourseDao;
    @Mock
    private StudentsCourseDao mockStudentsCourseDao;
    @Mock
    private Manager mockUiManager;
    @Mock
    private Group mockGroup;
    @Mock
    private Course mockCourse;
    @Mock
    private Student mockStudent;
    @Mock
    private StudentCourse mockStudentCourse;
    @Mock
    private List<Group> mockGroups;
    @Mock
    private List<Course> mockCourses;
    @Mock
    private List<Student> mockStudents;
    @Mock
    private List<StudentCourse> mockStudentCourses;
    @Mock
    private Iterator<Group> mockGroupIterator;
    @Mock
    private Iterator<Course> mockCoursesIterator;
    @Mock
    private Iterator<Student> mockStudentsIterator;
    @Mock
    private Iterator<StudentCourse> mockStudentCourseIterator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockConnector.getConnection()).thenReturn(mockConnection);
        doNothing().when(mockTableCreator).createTables(anyString());

        when(mockGenerator.generateGroups()).thenReturn(mockGroups);

        doCallRealMethod().when(mockGroups).forEach(any());
        when(mockGroups.iterator()).thenReturn(mockGroupIterator);
        when(mockGroupIterator.hasNext()).thenReturn(true, false);
        when(mockGroupIterator.next()).thenReturn(mockGroup);
        doNothing().when(mockGroupDao).create(mockGroup);

        when(mockGenerator.generateCourses()).thenReturn(mockCourses);

        doCallRealMethod().when(mockCourses).forEach(any());
        when(mockCourses.iterator()).thenReturn(mockCoursesIterator);
        when(mockCoursesIterator.hasNext()).thenReturn(true, false);
        when(mockCoursesIterator.next()).thenReturn(mockCourse);
        doNothing().when(mockCourseDao).create(mockCourse);

        when(mockGenerator.generateStudents()).thenReturn(mockStudents);
        doNothing().when(mockGenerator).randomAssignStudentsToGroups(mockStudents, mockGroups);

        doCallRealMethod().when(mockStudents).forEach(any());
        when(mockStudents.iterator()).thenReturn(mockStudentsIterator);
        when(mockStudentsIterator.hasNext()).thenReturn(true, false);
        when(mockStudentsIterator.next()).thenReturn(mockStudent);
        doNothing().when(mockStudentDao).create(mockStudent);

        when(mockGenerator.generateStudentsCourses(mockStudents)).thenReturn(mockStudentCourses);

        doCallRealMethod().when(mockStudentCourses).forEach(any());
        when(mockStudentCourses.iterator()).thenReturn(mockStudentCourseIterator);
        when(mockStudentCourseIterator.hasNext()).thenReturn(true, false);
        when(mockStudentCourseIterator.next()).thenReturn(mockStudentCourse);
        doNothing().when(mockStudentsCourseDao).create(mockStudentCourse);

        doNothing().when(mockUiManager).runApplicationMenu();
    }

    @Test
    void shouldCallCorrectMethodsForCreateDataBase() {
        Facade facade = new Facade(mockCourseDao, mockGroupDao, mockStudentDao, mockStudentsCourseDao,
            mockGenerator, mockTableCreator, mockUiManager);
        facade.runApplication();

        verify(mockTableCreator).createTables(anyString());

        verify(mockGenerator).generateGroups();
        verify(mockGroupDao).create(mockGroup);

        verify(mockGenerator).generateCourses();
        verify(mockCourseDao).create(mockCourse);

        verify(mockGenerator).generateStudents();
        verify(mockGenerator).randomAssignStudentsToGroups(mockStudents, mockGroups);
        verify(mockStudentDao).create(mockStudent);

        verify(mockGenerator).generateStudentsCourses(mockStudents);
        verify(mockStudentsCourseDao).create(mockStudentCourse);

        verify(mockUiManager).runApplicationMenu();
    }
}