package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    Set<Student> findAllByAgeBetween(int minAge, int maxAge);

    Collection<Student> findAllStudentsByFacultyNameIgnoreCase(String facultyName);

    Student findStudentByNameIgnoreCase(String name);

    @Query(value = "SELECT COUNT (name) FROM students", nativeQuery = true)
    Integer getStudentsCount();

    @Query(value = "SELECT AVG (age) FROM students", nativeQuery = true)
    Integer getAverageAge();


    @Query(value = "SELECT * from students ORDER BY id DESC LIMIT 5 ", nativeQuery = true)
    Collection<Student> getLastFiveStudents();
}
