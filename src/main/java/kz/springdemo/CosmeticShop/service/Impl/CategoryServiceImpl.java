package kz.springdemo.CosmeticShop.service.Impl;

import kz.springdemo.CosmeticShop.entities.Category;
import kz.springdemo.CosmeticShop.entities.Cosmetics;
import kz.springdemo.CosmeticShop.repository.CategoryRepository;
import kz.springdemo.CosmeticShop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.getOne(id);
    }
}
