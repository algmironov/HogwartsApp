package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import javax.validation.constraints.AssertTrue;

import java.util.HashSet;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.isNotNull;


@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        assertThat(studentController).isNotNull();
    }

    @Test
    public void testingGetAllStudents() throws Exception{
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students", String.class))
                .isNotNull();
    }

    @Test
    public void testingCreateStudent() throws Exception{
        Student student = new Student();
        student.setName("TestStudent");
        student.setAge(35);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/students", student, String.class))
                .isNotNull();
    }

    @Test
    public void testingEditStudent() throws Exception {
        final Long id = 8L;
        Student studentToEdit = new Student();
        studentToEdit.setName("TestStudent");
        studentToEdit.setAge(36);
        studentToEdit.setId(id);

        HttpEntity<Student> entity = new HttpEntity<>(studentToEdit);
        ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/students", HttpMethod.PUT,
                entity, Student.class, id);


        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testingGetStudentById() throws Exception{
        final Long id = 9L;
        Student studentToFind = new Student();
        studentToFind.setName("TestStudent");
        studentToFind.setAge(36);
        studentToFind.setId(id);

        HttpEntity<Student> entity = new HttpEntity<>(studentToFind);
        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/students/" + id,
                Student.class, entity, id);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/" + id, Student.class))
                .isEqualTo(studentToFind);
    }

    @Test
    public void testingGetFilteredStudents() throws Exception {
        final Long id = 9L;
        final int age = 36;
        Student student = new Student();
        student.setName("TestStudent");
        student.setAge(age);
        student.setId(id);
        Set<Student> testSet = new HashSet<>();
        testSet.add(student);

        HttpEntity<Set<Student>> entity = new HttpEntity<>(testSet);
        ResponseEntity<Set<Student>> response = restTemplate.exchange("http://localhost:" + port + "/students/filter/" + age
                , HttpMethod.GET, entity, new ParameterizedTypeReference<Set<Student>>(){});
        Set<Student> filteredStudents = response.getBody();
        assert filteredStudents != null;
        assertEquals(filteredStudents, testSet);

    }

    @Test
    public void testingGetStudentWithAgeBetweenMinAndMax() throws Exception {
        final Long id = 9L;
        final int age = 36;
        final int minAge = 35;
        final int maxAge = 40;

        Student student = new Student();
        student.setName("TestStudent");
        student.setAge(age);
        student.setId(id);
        Set<Student> testSet = new HashSet<>();
        testSet.add(student);

        HttpEntity<Set<Student>> entity = new HttpEntity<>(testSet);
        ResponseEntity<Set<Student>> response = restTemplate.exchange("http://localhost:" + port +
                        "/students/filter/ageBetween?minAge=" + minAge + "&maxAge=" + maxAge
                ,HttpMethod.GET, entity, new ParameterizedTypeReference<Set<Student>>(){});
        Set<Student> filteredStudents = response.getBody();
        assert filteredStudents != null;
        assertEquals(filteredStudents, testSet);

    }

    @Test
    public void testingGetFacultyOfStudentByStudentName() throws Exception {
        final Long id = 9L;
        final int age = 36;
        final String name = "TestStudent";
        Student student = new Student();
        student.setAge(age);
        student.setId(id);

        Faculty faculty = new Faculty(8L,"test", "white");

        HttpEntity<Faculty> entity = new HttpEntity<Faculty>(faculty);
        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/students/getFaculty/studentName?studentName=TestStudent",
                Faculty.class, entity, name);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), faculty);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/getFaculty/studentName&studentName=" + name, Faculty.class))
                .isNotNull();
    }

    @Test
    public void testingGetFacultyByStudentId() throws Exception {
        final Long id = 9L;
        final int age = 36;
        final String name = "TestStudent";
        Student student = new Student();
        student.setAge(age);
        student.setId(id);

        Faculty faculty = new Faculty(8L,"test", "white");

        HttpEntity<Faculty> entity = new HttpEntity<Faculty>(faculty);
        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/students/getFacultyById/" + id,
                Faculty.class, entity, name);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), faculty);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/getFacultyById/" + id, Faculty.class))
                .isNotNull();
    }

    @Test
    public void testingGetStudentsOfFaculty() throws Exception {
        final Long id = 9L;
        final int age = 36;
        final String name = "TestStudent";
        final String facultyName = "test";
        Student student = new Student();
        student.setAge(age);
        student.setId(id);
        student.setName(name);

        Set<Student> studentsOfFaculty = new HashSet<Student>();
        studentsOfFaculty.add(student);
        HttpEntity<Set<Student>> entity = new HttpEntity<>(studentsOfFaculty);

        ResponseEntity<Set<Student>> response = restTemplate.exchange("http://localhost:" + port +
                        "/students/studentsOfFaculty/" + facultyName
                ,HttpMethod.GET, entity, new ParameterizedTypeReference<Set<Student>>(){});
        Set<Student> foundStudents = response.getBody();
        assert foundStudents != null;
        assertEquals(foundStudents, studentsOfFaculty);
    }

    @Test
    public void testingDeleteStudent() throws Exception {
        final Long id = 11L;
        final int age = 36;
        final String name = "TestStudent";
        final String facultyName = "test";
        Student studentToDelete = new Student();
        studentToDelete.setAge(age);
        studentToDelete.setId(id);
        studentToDelete.setName(name);

        HttpEntity<Student> entity = new HttpEntity<>(studentToDelete);
        ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/students/{id}",
                HttpMethod.DELETE, null, Student.class, id);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}
