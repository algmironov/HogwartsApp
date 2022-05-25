package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("students")
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentServiceImpl;

    public StudentController(StudentService studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentServiceImpl.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student studentToEdit = studentServiceImpl.editStudent(student);
        if (studentToEdit == null) {
            logger.error("Student is not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentToEdit);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        Student studentToDelete = studentServiceImpl.findStudent(id);
        if (studentToDelete == null) {
            logger.error("Student with id=" + id + " is not found");
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        studentServiceImpl.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        try {
            Student student = studentServiceImpl.findStudent(id);
        } catch (Exception e) {
            logger.error("Couldn't find student with id= " + id);
        }
        return ResponseEntity.ok(studentServiceImpl.findStudent(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable("name") String name) {
        try {
            Student student = studentServiceImpl.findStudentByName(name);
        } catch (Exception e) {
            logger.error("Couldn't find user with name= " + name);
        }
        return ResponseEntity.ok(studentServiceImpl.findStudentByName(name));
    }

    @GetMapping("/filter/{age}")
    public ResponseEntity<Set<Student>> getFilteredStudents(@PathVariable int age) {
        Set<Student> filteredStudents= studentServiceImpl.getFilteredStudents(age);
        if (filteredStudents.isEmpty()) {
            logger.error("There is no student with age= " + age);
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(filteredStudents);
    }

    @GetMapping("/filter/ageBetween")
    public ResponseEntity<Set<Student>> getStudentWithAgeBetweenMinAndMax(@RequestParam int minAge, @RequestParam int maxAge) {
        Set<Student> studentWithAgeBetweenMinAndMax = studentServiceImpl.findAllByAgeBetween(minAge, maxAge);
        if (studentWithAgeBetweenMinAndMax.isEmpty()) {
            logger.error("There are no students in age between minAge= " + minAge + " and maxAge= " + maxAge);
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentWithAgeBetweenMinAndMax);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        if (studentServiceImpl.getAllStudents().isEmpty()) {
            logger.error("There are no students yet");
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentServiceImpl.getAllStudents());
    }

    @GetMapping("/getFaculty/studentName")
    public ResponseEntity<Faculty> getFacultyOfStudent(@RequestParam String studentName) {
        if (studentServiceImpl.findStudentByName(studentName) == null)  {
            logger.error("There is no such student with name = " + studentName);
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentServiceImpl.getFacultyByStudentName(studentName));
    }

    @GetMapping("/getFacultyById/{studentId}")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable Long studentId) {
        if (!studentServiceImpl.getAllStudents().contains(studentServiceImpl.findStudent(studentId)))  {
            logger.error("There is no such student with id = " + studentId);
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentServiceImpl.getFacultyByStudentId(studentId));
    }

    @GetMapping("/studentsOfFaculty/{facultyName}")
    public ResponseEntity<Collection<Student>> findAllStudentsOfFaculty(@PathVariable String facultyName) {
        Collection<Student> studentsOfFaculty = studentServiceImpl.findAllStudentsByFacultyName(facultyName);
        if (studentsOfFaculty.isEmpty()) {
            logger.error("The list *students of faculty* is empty");
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentsOfFaculty);
    }

    @GetMapping("/getStudentsCount")
    public ResponseEntity<Integer> getStudentsCount() {
        Integer studentsCount = studentServiceImpl.getStudentsCount();
        if (studentsCount == null) {
            logger.error("Student list is empty");
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentsCount);
    }

    @GetMapping("/getAverageAge")
    public ResponseEntity<Integer> getAverageAge() {
        Integer averageAge = studentServiceImpl.getAverageAge();
        if (averageAge == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/lastFive")
    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        Collection<Student> lastFiveStudents = studentServiceImpl.getLastFiveStudents();
        if (lastFiveStudents.isEmpty()) {
            logger.error("There are not much students in the list");
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lastFiveStudents);
    }

}
