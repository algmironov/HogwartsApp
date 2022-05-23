package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("students")
public class StudentController {

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentToEdit);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentServiceImpl.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentServiceImpl.findStudent(id);
        if (student == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable("name") String name) {
        Student student = studentServiceImpl.findStudentByName(name);
        if (student == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/filter/{age}")
    public ResponseEntity<Set<Student>> getFilteredStudents(@PathVariable int age) {
        Set<Student> filteredStudents= studentServiceImpl.getFilteredStudents(age);
        if (filteredStudents.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(filteredStudents);
    }

    @GetMapping("/filter/ageBetween")
    public ResponseEntity<Set<Student>> getStudentWithAgeBetweenMinAndMax(@RequestParam int minAge, @RequestParam int maxAge) {
        Set<Student> studentWithAgeBetweenMinAndMax = studentServiceImpl.findAllByAgeBetween(minAge, maxAge);
        if (studentWithAgeBetweenMinAndMax.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentWithAgeBetweenMinAndMax);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        if (studentServiceImpl.getAllStudents().isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentServiceImpl.getAllStudents());
    }

    @GetMapping("/getFaculty/studentName")
    public ResponseEntity<Faculty> getFacultyOfStudent(@RequestParam String studentName) {
        if (studentServiceImpl.findStudentByName(studentName) == null)  {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentServiceImpl.getFacultyByStudentName(studentName));
    }

    @GetMapping("/getFacultyById/{studentId}")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable Long studentId) {
        if (!studentServiceImpl.getAllStudents().contains(studentServiceImpl.findStudent(studentId)))  {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentServiceImpl.getFacultyByStudentId(studentId));
    }

    @GetMapping("/studentsOfFaculty/{facultyName}")
    public ResponseEntity<Collection<Student>> findAllStudentsOfFaculty(@PathVariable String facultyName) {
        Collection<Student> studentsOfFaculty = studentServiceImpl.findAllStudentsByFacultyName(facultyName);
        if (studentsOfFaculty.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentsOfFaculty);
    }

    @GetMapping("/getStudentsCount")
    public ResponseEntity<Integer> getStudentsCount() {
        Integer studentsCount = studentServiceImpl.getStudentsCount();
        if (studentsCount == null) {
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
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lastFiveStudents);
    }

}
