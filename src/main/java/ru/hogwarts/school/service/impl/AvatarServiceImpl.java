package ru.hogwarts.school.service.impl;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.awt.print.Pageable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {

    Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDirectory;

    private final AvatarRepository avatarRepository;
    private final StudentService studentServiceImpl;

    public AvatarServiceImpl(StudentService studentServiceImpl, AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
        this.studentServiceImpl = studentServiceImpl;
    }

    private @NotNull String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public void uploadAvatar(Long student_id, MultipartFile file) throws IOException {
        logger.info("Uploading avatar");
        Student studentToAddAvatar = studentServiceImpl.findStudent(student_id);
        Path filePath = Path.of(avatarsDirectory, student_id + "." + getExtensions(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os,1024)) {

            bis.transferTo(bos);

        }
        Avatar avatar = findByStudentId(student_id);
        avatar.setStudent(studentToAddAvatar);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        studentToAddAvatar.setAvatar(avatar);
        avatarRepository.save(avatar);
    }

    @Override
    public List<Avatar> findAll(Integer pageNumber, Integer pageSize) {
        logger.info("Providing list of all avatars");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    @Override
    public Avatar findByStudentId(Long id) {
        logger.info("Searching avatar of user with id = " + id);
        return avatarRepository.findByStudentId(id).orElse(new Avatar());
    }


}
