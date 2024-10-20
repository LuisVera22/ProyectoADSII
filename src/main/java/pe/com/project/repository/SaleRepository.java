package pe.com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.project.model.entity.SaleEntity;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Integer> {
    
}
