package pe.com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.project.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{
    UserEntity findByEmail(String email);
}