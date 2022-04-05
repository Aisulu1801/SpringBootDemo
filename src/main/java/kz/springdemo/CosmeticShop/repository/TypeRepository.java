package kz.springdemo.CosmeticShop.repository;

import kz.springdemo.CosmeticShop.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {
    List<Type>findAllByCategoryId(Long id);

    List<Type>findAll();
}
