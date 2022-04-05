package kz.springdemo.CosmeticShop.repository;

import kz.springdemo.CosmeticShop.entities.Category;
import kz.springdemo.CosmeticShop.entities.Cosmetics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
