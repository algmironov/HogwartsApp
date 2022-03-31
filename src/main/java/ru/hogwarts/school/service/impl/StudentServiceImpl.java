package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    HashMap<Long, Student> students = new HashMap<>();
    private long studentId = 0;

    @Override
    public Student createStudent(Student student) {
        student.setId(++studentId);
        students.put(studentId, student);
        return student;
    }

    @Override
    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            students.put(student.getId(), student);
            return student;
        }
        return null;
    }

    @Override
    public Student deleteStudent(long id) {
        if (students.containsKey(id)) {
            return students.remove(id);
        }
        return null;
    }

    @Override
    public Student findStudent(long id) {
        if (students.containsKey(id)) {
            return students.get(id);
        }
        return null;
    }

    @Override
    public Set<Student> getFilteredStudents(int age) {
        return getAllStudents().stream().
                filter(student -> student.getAge() == age).collect(Collectors.toSet());
    }

    @Override
    public Set<Student> getAllStudents() {
        return new HashSet<>(students.values());
    }
}
