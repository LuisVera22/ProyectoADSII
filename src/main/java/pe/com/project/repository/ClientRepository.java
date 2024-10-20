package pe.com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.project.model.entity.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {
    
}
