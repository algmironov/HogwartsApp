package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyServiceImpl implements FacultyService {

    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Creating new faculty");
        facultyRepository.save(faculty);
        return facultyRepository.findByName(faculty.getName());
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Editing faculty:" + faculty);
        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(long id) {
        logger.info("Deleting faculty with id= " + id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty findFaculty(long id) {
        logger.info("found faculty with id= " + id );
        return facultyRepository.findById(id).get();
    }

    @Override
    public Set<Faculty> filteredByColorFaculties(String color) {
        logger.info("Searching for faculty with color= " + color );
        return facultyRepository.findAllByColor(color);
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        logger.info("Requesting for all faculties");
        return facultyRepository.findAll();
    }

    @Override
    public Collection<Faculty> findAllByNameOrColor(String name, String color) {
        logger.info("Searching for faculty with name= " + name + " and/or color= " + color);
        return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @Override
    public String getLongestFacultyName() {
        logger.info("Searching for longest faculty name");

        List<String> names = facultyRepository.findAll().parallelStream().
                map(Faculty::getName).collect(Collectors.toList()).
                parallelStream().sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
        return names.get(names.size() -1);
    }

    @Override
    public Integer getResultWithModifiedParallelStream() {
        return Stream.iterate(1, a -> a +1).parallel()
                .limit(1_000_000).
                reduce(0, Integer::sum);
    }
}
