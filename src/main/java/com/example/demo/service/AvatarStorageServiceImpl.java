package com.example.demo.service;

import com.example.demo.dao.AvatarImageRepository;
import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.AvatarImage;
import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.*;

@Service
public class AvatarStorageServiceImpl implements AvatarStorageService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarStorageServiceImpl.class);

    private final AvatarImageRepository avatarImageRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    @Value("${file.storage.path}")
    private String path;

    @Autowired
    public AvatarStorageServiceImpl(AvatarImageRepository avatarImageRepository,
                                    UserRepository userRepository, CourseRepository courseRepository) {
        this.avatarImageRepository = avatarImageRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    @Override
    public void saveUserAvatar(String username, String contentType, String nativeName, InputStream is) {
        Optional<AvatarImage> opt = avatarImageRepository.findUserByUsername(username);
        AvatarImage avatarImage;
        String filename;
        if (opt.isEmpty()) {
            filename = UUID.randomUUID().toString();
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(IllegalArgumentException::new);
            avatarImage = new AvatarImage(null, contentType, filename, nativeName, user);
        } else {
            avatarImage = opt.get();
            filename = avatarImage.getFilename();
            avatarImage.setContentType(contentType);
            avatarImage.setNativeName(nativeName);
        }
        avatarImageRepository.save(avatarImage);

        try (OutputStream os = Files
                .newOutputStream(Path.of(path, filename), CREATE, WRITE, TRUNCATE_EXISTING)) {
            is.transferTo(os);
        } catch (Exception ex) {
            logger.error("Can't write to file {}", filename, ex);
            throw new IllegalStateException(ex);
        }
    }

    @Transactional
    @Override
    public void saveCourseAvatar(Long id, String contentType, String nativeName, InputStream is) {
        Optional<AvatarImage> opt = avatarImageRepository.findCourseById(id);
        AvatarImage avatarImage;
        String filename;
        if (opt.isEmpty()) {
            filename = UUID.randomUUID().toString();
            Course course = courseRepository.findCourseById(id)
                    .orElseThrow(IllegalArgumentException::new);
            avatarImage = new AvatarImage(null, contentType, filename, nativeName, course);
        } else {
            avatarImage = opt.get();
            filename = avatarImage.getFilename();
            avatarImage.setContentType(contentType);
            avatarImage.setNativeName(nativeName);
        }
        avatarImageRepository.save(avatarImage);

        try (OutputStream os = Files
                .newOutputStream(Path.of(path, filename), CREATE, WRITE, TRUNCATE_EXISTING)) {
            is.transferTo(os);
        } catch (Exception ex) {
            logger.error("Can't write to file {}", filename, ex);
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<String> getContentTypeByUser(String username) {
        return avatarImageRepository.findUserByUsername(username)
                .map(AvatarImage::getContentType);
    }

    @Override
    public Optional<String> getContentTypeByCourse(Long id) {
        return avatarImageRepository.findCourseById(id)
                .map(AvatarImage::getContentType);
    }

    @Override
    public Optional<byte[]> getAvatarImageByUser(String username) {
        return avatarImageRepository.findUserByUsername(username)
                .map(AvatarImage::getFilename)
                .map(filename -> {
                    try {
                        return Files.readAllBytes(Path.of(path, filename));
                    } catch (IOException ex) {
                        logger.error("Can't read file {}", filename, ex);
                        throw new IllegalStateException(ex);
                    }
                });
    }

    @Override
    public Optional<byte[]> getAvatarImageByCourse(Long id) {
        return avatarImageRepository.findCourseById(id)
                .map(AvatarImage::getFilename)
                .map(filename -> {
                    try {
                        return Files.readAllBytes(Path.of(path, filename));
                    } catch (IOException ex) {
                        logger.error("Can't read file {}", filename, ex);
                        throw new IllegalStateException(ex);
                    }
                });
    }

    @Override
    public Optional<byte[]> getNullAvatarImage(String avatar) {
        try {
            if (avatar.equals("course")) {
                return Optional.of(Files.readAllBytes(Path.of(path, "no_avatar")));
            }
            if (avatar.equals("user")) {
                return Optional.of(Files.readAllBytes(Path.of(path, "unnamed")));
            }
            return Optional.empty();
        } catch (IOException ex) {
            logger.error("Can't read file {}", "no_avatar", ex);
            throw new IllegalStateException(ex);
        }
    }


}
