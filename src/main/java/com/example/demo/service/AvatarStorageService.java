package com.example.demo.service;

import java.io.InputStream;
import java.util.Optional;

public interface AvatarStorageService {

  void saveUserAvatar(String username, String contentType, String nativeName, InputStream is);

  void saveCourseAvatar(Long id, String contentType, String nativeName, InputStream is);

  Optional<String> getContentTypeByUser(String username);

  Optional<String> getContentTypeByCourse(Long id);

  Optional<byte[]> getAvatarImageByUser(String username);

  Optional<byte[]> getAvatarImageByCourse(Long id);

  Optional<byte[]> getNullAvatarImage(String avatar);

}
