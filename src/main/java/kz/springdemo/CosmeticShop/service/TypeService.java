package kz.springdemo.CosmeticShop.service;

import kz.springdemo.CosmeticShop.entities.Category;
import kz.springdemo.CosmeticShop.entities.Type;

import java.util.List;

public interface TypeService {
    List<Type>getAllTypeByCategoryId(Long id);
    List<Type>getAllType();
    Type getType(Long id);
}
