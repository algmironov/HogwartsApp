package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    private final FacultyService facultyService;

    public StudentServiceImpl(StudentRepository studentRepository, FacultyService facultyService) {
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }

    public final Object flag = new Object();

    @Override
    public Student createStudent(Student student) {
        logger.info("Creating student:" + student);
        return studentRepository.save(student);
    }

    @Override
    public Student editStudent(Student student) {
        logger.info("Editing student " + student);
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        logger.info("Student with id=" + id + " has been deleted successfully!");
        studentRepository.deleteById(id);
    }

    @Override
    public Student findStudent(long id) {
        logger.debug("Finding student with id = " + id );
        return studentRepository.findById(id).get();
    }

    @Override
    public Student findStudentByName(String name) {
        logger.info("Finding user with name= " + name );
        return studentRepository.findStudentByNameIgnoreCase(name);
    }

    @Override
    public Set<Student> getFilteredStudents(int age) {
        logger.info("Returning filtered by age students");
        return getAllStudents().stream().
                filter(student -> student.getAge() == age).collect(Collectors.toSet());
    }

    @Override
    public Collection<Student> getAllStudents() {
        logger.info("Printing collection with all students");
        return studentRepository.findAll();
    }


    @Override
    public Set<Student> findAllByAgeBetween(int minAge, int maxAge) {
        logger.info("Requesting students in age between: minAge = " + minAge + " and maxAge = " + maxAge);
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Finding faculty of student with id= " +studentId);
        Student student = findStudent(studentId);
        return student.getFaculty();
    }

    @Override
    public Faculty getFacultyByStudentName(String studentName) {
        logger.info("Finding faculty of student with name= " + studentName);
        return findStudentByName(studentName).getFaculty();
    }


    @Override
    public Collection<Student> findAllStudentsByFacultyName (String facultyName) {
        logger.info("Requesting students of faculty:" + facultyName);
        return studentRepository.findAllStudentsByFacultyNameIgnoreCase(facultyName);
    }

    @Override
    public void setFacultyById(Student student, Long id) {
        logger.info("Setting faculty with id= " + id + " for student " + student);
        Faculty faculty = facultyService.findFaculty(id);
        student.setFaculty(faculty);
    }

    @Override
    public Integer getStudentsCount() {
        logger.info("Calculating students count");
        return studentRepository.getStudentsCount();
    }

    @Override
    public Integer getAverageAge() {
        logger.info("Calculating average age");
        return studentRepository.getAverageAge();
    }

    @Override
    public Collection<Student> getLastFiveStudents() {
        logger.info("Searching for last 5 students");
        return studentRepository.getLastFiveStudents();
    }

    @Override
    public List<String> getStudentsWithNamesStartingWithALetter() {
        logger.info("Requesting students' names with A letter on the start");
        return  studentRepository.findAll().parallelStream().map(Student::getName).collect(Collectors.toList()).
                parallelStream().filter(n -> n.startsWith("A")).
                sorted(Comparator.naturalOrder()).collect(Collectors.toList()).
                parallelStream().map(String::toUpperCase).collect(Collectors.toList());
    }

    @Override
    public Double getAverageAgeUsingParallelStream() {
        logger.info("Requesting average age");
        return studentRepository.findAll().parallelStream().mapToInt(Student::getAge).average().getAsDouble();
    }

    public void printStudent(int index) {
        List<String> names = studentRepository.
                findAll().stream().
                map(Student::getName).
                collect(Collectors.toList());
            System.out.println(names.get(index));

    }
    public void printSynchronizedStudent(int index) {
        synchronized (flag) {
            List<String> names = studentRepository.
                    findAll().stream().
                    map(Student::getName).
                    collect(Collectors.toList());
            System.out.println(names.get(index));
        }
    }

    @Override
    public void studentsThread() {
        printStudent(0);
        printStudent(1);
        new Thread(() -> {
            printStudent(2);
            printStudent(3);
        }).start();
        new Thread(() -> {
            printStudent(4);
            printStudent(5);
        }).start();
        printStudent(6);
        printStudent(7);

    }

    @Override
    public void studentsSynchronizedThread() {
        printSynchronizedStudent(0);
        printSynchronizedStudent(1);
        new Thread(() -> {
            printSynchronizedStudent(2);
            printSynchronizedStudent(3);
        }).start();
        new Thread(() -> {
            printSynchronizedStudent(4);
            printSynchronizedStudent(5);
        }).start();
        printSynchronizedStudent(6);
        printSynchronizedStudent(7);

    }

}
