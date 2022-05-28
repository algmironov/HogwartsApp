package ru.hogwarts.school.controller;

import org.hibernate.cfg.CreateKeySecondPass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/faculties")
public class FacultyController {
    Logger logger = LoggerFactory.getLogger(FacultyController.class);

    private final FacultyService facultyServiceImpl;

    public FacultyController(FacultyService facultyServiceImpl) {
        this.facultyServiceImpl = facultyServiceImpl;
    }


    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        try {
            facultyServiceImpl.createFaculty(faculty);
            return ResponseEntity.ok(faculty);
        } catch (Exception e) {
            logger.error("There is missing parameter that caused error");
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        try {
            Faculty facultyToEdit = facultyServiceImpl.editFaculty(faculty);
            return ResponseEntity.ok(facultyToEdit);
        } catch (Exception e) {
            logger.error("Faculty is not found");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        try {
            Faculty faculty = facultyServiceImpl.findFaculty(id);
        } catch (Exception e) {
            logger.error("Faculty with id=" + id + " is not found");
        }
        facultyServiceImpl.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        try {
            Faculty faculty = facultyServiceImpl.findFaculty(id);
            return ResponseEntity.ok(faculty);
        } catch (Exception e) {
            logger.error("Couldn't find faculty with id= " + id);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter/{color}")
    public ResponseEntity<Set<Faculty>> filteredByColorFaculties(@PathVariable String color) {
        Set<Faculty> filteredSet = facultyServiceImpl.filteredByColorFaculties(color);
        if (filteredSet.isEmpty()) {
            logger.error("Couldn't find faculties with color= " + color);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(filteredSet);
    }

    @GetMapping()
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        try {
            return ResponseEntity.ok(facultyServiceImpl.getAllFaculties());
        } catch (Exception e) {
            logger.error("Couldn't find faculties");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/{name}&{color}")
    public ResponseEntity<Collection<Faculty>> findByNameOrColor(@PathVariable("name") String name, @PathVariable("color") String color) {
        Collection<Faculty> foundFaculties = facultyServiceImpl.findAllByNameOrColor(name, color);
        if (foundFaculties.isEmpty()) {
            logger.error("Couldn't find faculties with name= " + name + " and color= " + color);
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(foundFaculties);
    }

    @GetMapping("/getLongestFacultyName")
    public ResponseEntity<String> getLongestFacultyName() {
        return ResponseEntity.ok(facultyServiceImpl.getLongestFacultyName());
    }

    @GetMapping("/task4")
    public ResponseEntity<Integer> getResultWithModifiedParallelStream() {
        return ResponseEntity.ok(facultyServiceImpl.getResultWithModifiedParallelStream());
    }
}
