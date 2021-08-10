package ua.com.foxminded.school.domain;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import ua.com.foxminded.school.dao.entity.Course;
import ua.com.foxminded.school.dao.entity.Group;
import ua.com.foxminded.school.dao.entity.Student;
import ua.com.foxminded.school.dao.entity.StudentCourse;

import java.util.*;
import java.util.stream.IntStream;

public class DataGenerator implements Generator {
    private static final String COURSE_NAME_PATTERN = "(Associate Degree in )|(Bachelor of )|(Master of )";
    private static final String GROUP_NAME_PATTERN = "??-##";
    private static final String EMPTY = "";
    private static final String STRING_FORMAT_FOR_DESCRIPTION = "%s is a kind of discipline at school.";

    public List<Group> generateGroups() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"), new RandomService());
        List<Group> groups = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(id -> {
            String groupName = fakeValuesService.bothify(GROUP_NAME_PATTERN);
            groups.add(new Group(id, groupName));
        });
        return groups;
    }

    public List<StudentCourse> generateStudentsCourses(List<Student> students) {
        List<StudentCourse> result = new ArrayList<>();
        students.forEach(student ->
            new Random()
                .ints(1, 11)
                .distinct()
                .limit(3)
                .forEach(courseId -> result.add(new StudentCourse(student.getId(), courseId))));
        return result;
    }

    public List<Course> generateCourses() {
        List<Course> courses = new ArrayList<>();
        Set<String> uniqueCourseNames = getUniqueCourseNames();
        int id = 1;
        for (String uniqueName : uniqueCourseNames) {
            String description = String.format(STRING_FORMAT_FOR_DESCRIPTION, uniqueName);
            courses.add(new Course(id, uniqueName, description));
            id++;
        }
        return courses;
    }

    public List<Student> generateStudents() {
        Faker faker = new Faker();
        List<Student> students = new ArrayList<>();
        IntStream.rangeClosed(1, 200).forEach(id -> {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            int groupId = 0;
            students.add(new Student(id, groupId, firstName, lastName));
        });
        return students;
    }

    private Set<String> getUniqueCourseNames() {
        Faker faker = new Faker();
        Set<String> uniqueCourseNames = new HashSet<>();

        while (uniqueCourseNames.size() < 10) {
            String courseName = faker.educator().course().replaceAll(COURSE_NAME_PATTERN, EMPTY);
            uniqueCourseNames.add(courseName);
        }
        return uniqueCourseNames;
    }

    public void randomAssignStudentsToGroups(List<Student> students, List<Group> groups) {
        Collections.shuffle(students);

        int max = 30;
        int min = 10;

        for (Group group : groups) {
            int i = 0;
            int randomQuantityStudents = new Random().nextInt((max - min) + 1) + min;
            while (i != randomQuantityStudents) {
                students.forEach(student -> student.setGroupId(group.getId()));
                i++;
            }
            if (i > students.size()) {
                students = students.subList(0, students.size());
            } else {
                students = students.subList(i, students.size());
            }
        }
        students.forEach(student -> student.setGroupId(0));
    }
}