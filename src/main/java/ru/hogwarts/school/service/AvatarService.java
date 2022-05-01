package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.Optional;

public interface AvatarService {

    Avatar findByStudentId(Long Id);

    void uploadAvatar(Long id, MultipartFile avatar) throws IOException;
}
