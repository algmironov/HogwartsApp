package ru.hogwarts.school.repository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findByName(String name);

    Collection<Faculty> findAllByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    Set<Faculty> findAllByColor(String color);

    @NotNull Optional<Faculty> findById(@NotNull Long id);
}
