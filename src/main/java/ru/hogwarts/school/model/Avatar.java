package ru.hogwarts.school.model;

import javax.persistence.*;
import java.util.Arrays;

@Entity(name="avatars")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;
    private long fileSize;
    private String mediaType;

    private byte[] data;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Avatar(Long id, String filePath, long fileSize, String mediaType, byte[] data, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.student = student;
    }

    public Avatar() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avatar)) return false;

        Avatar avatar = (Avatar) o;

        if (getFileSize() != avatar.getFileSize()) return false;
        if (!getId().equals(avatar.getId())) return false;
        if (!getFilePath().equals(avatar.getFilePath())) return false;
        if (!getMediaType().equals(avatar.getMediaType())) return false;
        if (!Arrays.equals(getData(), avatar.getData())) return false;
        return getStudent() != null ? getStudent().equals(avatar.getStudent()) : avatar.getStudent() == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getFilePath().hashCode();
        result = 31 * result + (int) (getFileSize() ^ (getFileSize() >>> 32));
        result = 31 * result + getMediaType().hashCode();
        result = 31 * result + Arrays.hashCode(getData());
        result = 31 * result + (getStudent() != null ? getStudent().hashCode() : 0);
        return result;
    }
}
