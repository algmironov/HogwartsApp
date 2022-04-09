package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("faculties")
public class FacultyController {

    private final FacultyService facultyServiceImpl;

    public FacultyController(FacultyService facultyServiceImpl) {
        this.facultyServiceImpl = facultyServiceImpl;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyServiceImpl.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty facultyToEdit = facultyServiceImpl.editFaculty(faculty);
        if (facultyToEdit == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(facultyToEdit);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyServiceImpl.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyServiceImpl.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/filter/{color}")
    public ResponseEntity<Set<Faculty>> filteredByColorFaculties(@PathVariable String color) {
        Set<Faculty> filteredSet = facultyServiceImpl.filteredByColorFaculties(color);
        if (filteredSet.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(filteredSet);
    }

    @GetMapping()
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        if (facultyServiceImpl.getAllFaculties().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(facultyServiceImpl.getAllFaculties());
    }
}
