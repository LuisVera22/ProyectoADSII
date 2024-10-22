package pe.com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.project.model.entity.SaleEntity;
import pe.com.project.repository.SaleRepository;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Transactional(readOnly = true)
    public List<SaleEntity> findAll() {
        return saleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SaleEntity> findById(int id) {
        return saleRepository.findById(id);
    }

    @Transactional
    public SaleEntity save(SaleEntity saleEntity) {
        return saleRepository.save(saleEntity);
    }

    @Transactional
    public void deleteById(int id) {
        saleRepository.deleteById(id);
    }
}

