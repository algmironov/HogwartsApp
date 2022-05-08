package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final FacultyService facultyService;

    public StudentServiceImpl(StudentRepository studentRepository, FacultyService facultyService) {
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }

    @Override
    public Student createStudent(Student student) {

        return studentRepository.save(student);
    }

    @Override
    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public Student findStudentByName(String name) {
        return studentRepository.findStudentByNameIgnoreCase(name);
    }

    @Override
    public Set<Student> getFilteredStudents(int age) {
        return getAllStudents().stream().
                filter(student -> student.getAge() == age).collect(Collectors.toSet());
    }

    @Override
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    @Override
    public Set<Student> findAllByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty getFacultyByStudentId(Long studentId) {
        Student student = findStudent(studentId);
        return student.getFaculty();
    }

    @Override
    public Faculty getFacultyByStudentName(String studentName) {
        return findStudentByName(studentName).getFaculty();
    }


    @Override
    public Collection<Student> findAllStudentsByFacultyName (String facultyName) {
        return studentRepository.findAllStudentsByFacultyNameIgnoreCase(facultyName);
    }

    @Override
    public void setFacultyById(Student student, Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        student.setFaculty(faculty);
    }

}
