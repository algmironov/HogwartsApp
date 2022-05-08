package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Set;

public interface StudentService {

    Student createStudent(Student student);

    Student editStudent(Student student);

    void deleteStudent(long id);

    Student findStudent(long id);

    Set<Student> getFilteredStudents(int age);

    Collection<Student> getAllStudents();

    Set<Student> findAllByAgeBetween(int minAge, int maxAge);

    Faculty getFacultyByStudentId(Long studentId);

    Faculty getFacultyByStudentName(String studentName);

    Collection<Student> findAllStudentsByFacultyName(String facultyName);

    Student findStudentByName(String studentName);

    void setFacultyById(Student student, Long id);
}
