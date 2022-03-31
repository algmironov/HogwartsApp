package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.Set;


public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty editFaculty(Faculty faculty);

    Faculty deleteFaculty(long id);

    Faculty findFaculty(long id);

    Set<Faculty> filteredByColorFaculties(String color);

    Collection<Faculty> getAllFaculties();


}
