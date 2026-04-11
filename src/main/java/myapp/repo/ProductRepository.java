package myapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import myapp.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}