package kz.springdemo.CosmeticShop.service.Impl;

import kz.springdemo.CosmeticShop.entities.Type;
import kz.springdemo.CosmeticShop.repository.TypeRepository;
import kz.springdemo.CosmeticShop.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;
    @Override
    public List<Type> getAllTypeByCategoryId(Long id) {
        return typeRepository.findAllByCategoryId(id);
    }

    @Override
    public List<Type> getAllType() {
        return typeRepository.findAll();
    }

    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

}


