package pe.com.project.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import pe.com.project.model.entity.ProductEntity;

public interface ProductService {
    List<ProductEntity>productList();
    void productCreate(ProductEntity newProduct, MultipartFile image);
    ProductEntity searchById(Integer id);
    void deleteProduct(Integer id);
}
