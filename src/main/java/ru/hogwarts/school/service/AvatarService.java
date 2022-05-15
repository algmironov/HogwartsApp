package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AvatarService {

    Avatar findByStudentId(Long Id);

    void uploadAvatar(Long id, MultipartFile avatar) throws IOException;

    List<Avatar> findAll(Integer pageNumber, Integer ageSize);
}
