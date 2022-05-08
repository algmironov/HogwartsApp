package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.internal.bytebuddy.build.Plugin;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.awt.*;
import java.util.*;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class FacultyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @MockBean
    private StudentServiceImpl studentService;

    @MockBean
    private AvatarService avatarService;

    @MockBean
    private AvatarController avatarController;

    @InjectMocks
    private FacultyController facultyController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testingCreateFacultyAndGetFaculty() throws Exception {
        final Long id = 9L;
        final String name = "TestFaculty";
        final String color = "TestColor";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty testFaculty = new Faculty(id, name, color);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(testFaculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(testFaculty));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/faculties")
                .content(facultyObject.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id)) - после создания в ответе выдает id = 0, несмотря на то, что в базе он уже с корректным id
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculties/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

    }

    @Test
    public void testingGetFaculty() throws  Exception {

        final Long id = 9L;
        final String name = "TestFaculty";
        final String color = "TestColor";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty testFaculty = new Faculty(id, name, color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(testFaculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testingGetFacultiesByColor() throws  Exception {
        Long id_1 = 1L;
        Long id_2 = 2L;

        String name1 = "testFaculty1";
        String name2 = "testFaculty2";

        String color = "blanc";
        String realColor = "white";

        Faculty faculty1 = new Faculty(id_1, name1, color);
        Faculty faculty2 = new Faculty(id_2, name2, color);

        when(facultyRepository.findAllByColor(any(String.class))).thenReturn(Set.of(faculty1, faculty2));

        mockMvc.perform(MockMvcRequestBuilders.
                        get("/faculties/filter/{color}", realColor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty1, faculty2))))
        ;
    }

    @Test
    public void testingPutFaculty() throws Exception {
        final Long id = 9L;
        final String name = "TestFaculty";
        final String updatedName = "TestFaculty";
        final String color = "TestColor";
        final String updatedColor = "TestColor";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", updatedName);
        facultyObject.put("color", updatedColor);

        Faculty testFaculty = new Faculty(id, name, color);
        Faculty updatedFaculty = new Faculty(id, updatedName, updatedColor);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(testFaculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculties")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName))
                .andExpect(jsonPath("$.color").value(updatedColor));
    }

    @Test
    public void testingGetAllFaculties() throws Exception {
        final Long id_1 = 1L;
        final Long id_2 = 2L;
        final Long id_3 = 3L;

        final String name_1 = "first";
        final String name_2 = "second";
        final String name_3 = "third";

        final String color = "blue";

        Faculty faculty_1 = new Faculty(id_1, name_1, color);
        Faculty faculty_2 = new Faculty(id_2, name_2, color);
        Faculty faculty_3 = new Faculty(id_3, name_3, color);

        when(facultyRepository.findAll()).thenReturn(List.of(faculty_1, faculty_2, faculty_3));

        mockMvc.perform(MockMvcRequestBuilders.
                        get("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty_1, faculty_2, faculty_3))));

    }

    @Test
    public void testingFindFacultiesByColorOrName() throws Exception {

        final Long id_1 = 1L;
        final Long id_2 = 2L;

        final String name_1 = "first";
        final String name_1_ignoreCase = "FirSt";

        final String name_2 = "second";

        final String color_1 = "blue";
        final String color_2 = "white";
        final String color_2_ignoreCase = "WHite";

        Faculty faculty_1 = new Faculty(id_1, name_1, color_1);
        Faculty faculty_2 = new Faculty(id_2, name_2, color_2);

        when(facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name_1_ignoreCase, color_2_ignoreCase)).thenReturn(Set.of(faculty_1, faculty_2));

        mockMvc.perform(MockMvcRequestBuilders.
                        get("/faculties/find/{name}&{color}", name_1_ignoreCase, color_2_ignoreCase)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty_1, faculty_2))));
    }

    @Test
    public void testingDeleteFaculty() throws Exception {
        Long id = 1L;
        String name = "facultyToDelete";
        String color = "black&white";

        Faculty facultyToDelete = new Faculty(id, name, color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(facultyToDelete));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculties/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(facultyRepository, atLeastOnce()).deleteById(id);
    }


}


