package ua.com.foxminded.school.domain;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.dao.entity.Group;
import ua.com.foxminded.school.dao.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataGeneratorTest {

    private static final String EMPTY = "";
    private static final String GROUP_NAME_PATTERN = "?? - ##";
    private static final String GROUP_NAME_REGEXP = "\\w{2} - \\d{2}";


    @Test
    void shouldGenerateCorrectGroupsNames() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"), new RandomService());

        String groupName = fakeValuesService.bothify(GROUP_NAME_PATTERN);
        Matcher groupNameMatcher = Pattern.compile(GROUP_NAME_REGEXP).matcher(groupName);

        assertTrue(groupNameMatcher.find());
    }

    @Test
    void shouldGenerateCorrectStudentsCourses() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, 4, "Mikhail", "Rumors"));
        students.add(new Student(2, 3, "Stephan", "Rivers"));

        DataGenerator generator = new DataGenerator();
        int actual = generator.generateStudentsCourses(students).size();
        int expected = 6;
        assertEquals(expected, actual);
    }

    @Test
    void shouldGenerateCorrectCourses() {
        DataGenerator generator = new DataGenerator();
        int actual = generator.generateCourses().size();
        int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    void shouldGenerateCorrectStudents() {
        DataGenerator generator = new DataGenerator();
        int actual = generator.generateStudents().size();
        int expected = 200;
        assertEquals(expected, actual);
    }

    @Test
    void shouldRandomAssignStudentsToGroups() {
        DataGenerator generator = new DataGenerator();
        List<Student> students = generator.generateStudents();
        List<Group> groups = generator.generateGroups();
        generator.randomAssignStudentsToGroups(students, groups);

        String actual = EMPTY;
        String expected = "Ok";
        for (int i = 1; i < 11; i++) {
            int finalI = i;
            int count = (int) students.stream().filter(student -> student.getGroupId() == finalI).count();
            if ((count > 10 && count <= 30) || count == 0) {
                actual = "Ok";
            } else {
                actual = "Failure";
            }
        }
        assertEquals(expected, actual);
    }
}