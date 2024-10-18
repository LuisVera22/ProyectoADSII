package pe.com.project.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import pe.com.project.model.entity.UserEntity;

public interface UserService {
    List<UserEntity>userList();
    void userCreate(UserEntity newUser, MultipartFile photo);
    UserEntity searchUserById(String dni);
    void updateUser(String dni, UserEntity updatedUser);
    void deleteUser(String dni);
    boolean validateUser(UserEntity userEntity);
}