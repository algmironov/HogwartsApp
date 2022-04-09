package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
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

    @GetMapping("/filter/{age}")
    public ResponseEntity<Set<Student>> getFilteredStudents(@PathVariable int age) {
        Set<Student> filteredStudents= studentServiceImpl.getFilteredStudents(age);
        if (filteredStudents.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(filteredStudents);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        if (studentServiceImpl.getAllStudents().isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(studentServiceImpl.getAllStudents());
    }
}
