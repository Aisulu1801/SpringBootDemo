package kz.springdemo.CosmeticShop.service;

import kz.springdemo.CosmeticShop.entities.Category;
import kz.springdemo.CosmeticShop.entities.Cosmetics;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategory(Long id);
}
