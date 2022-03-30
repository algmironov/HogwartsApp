package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    HashMap<Long, Faculty> faculties = new HashMap<>();
    private long facultyId = 0;

    @Override
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++facultyId);
        faculties.put(facultyId, faculty);
        return faculty;
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            faculties.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    @Override
    public Faculty deleteFaculty(long id) {
        if (faculties.containsKey(id)) {
            return faculties.remove(id);
        }
        return null;
    }

    @Override
    public Faculty findFaculty(long id) {
        if (faculties.containsKey(id)) {
            return faculties.get(id);
        }
        return null;
    }

    @Override
    public Set<Faculty> filteredByColorFaculties(String color) {

        return getAllFaculties().stream().
                filter(faculty -> faculty.getColor().equals(color)).
                collect(Collectors.toSet());
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }
}
