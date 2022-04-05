package kz.springdemo.CosmeticShop.service.Impl;

import kz.springdemo.CosmeticShop.entities.Cosmetics;
import kz.springdemo.CosmeticShop.repository.CosmeticsRepository;
import kz.springdemo.CosmeticShop.service.CosmeticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CosmeticsServiceImpl implements CosmeticsService {

    @Autowired
    private CosmeticsRepository cosmeticsRepository;

    @Override
    public Cosmetics addCosmetic(Cosmetics cosmetic) {
        return cosmeticsRepository.save(cosmetic);
    }

    @Override
    public List<Cosmetics> getAllCosmetics(String keyword) {
        if(keyword!=null){
            return cosmeticsRepository.findAll(keyword);
        }
        return cosmeticsRepository.findAll();
    }


    @Override
    public Cosmetics getCosmetic(Long id) {

        return cosmeticsRepository.getOne(id);
    }

    @Override
    public void deleteCosmeticById(Long id) {
        cosmeticsRepository.deleteById(id);
    }

    @Override
    public void deleteCosmetic(Cosmetics cosmetics) {
        cosmeticsRepository.delete(cosmetics);
    }


    @Override
    public Cosmetics saveCosmetic(Cosmetics cosmetic) {
        return cosmeticsRepository.save(cosmetic);
    }

    @Override
    public List<Cosmetics> getAllCosmeticsByCategoryId(Long id) {
        return cosmeticsRepository.findAllByCategoryId(id);
    }

    @Override
    public List<Cosmetics> getAllCosmeticsByTypeId(Long id) {
        return cosmeticsRepository.findAllByTypeId(id);
    }

    @Override
    public Cosmetics getCosmeticsById(Long id) {
        return cosmeticsRepository.findById(id).get();
    }

    @Override
    public List<Cosmetics> getAllCosmetic() {
        return cosmeticsRepository.findAllByAmountGreaterThanOrderByPriceDesc(0);
    }

    @Override
    public List<Cosmetics> getAllProduct() {
        return cosmeticsRepository.findAllByAmountGreaterThanOrderByPriceAsc(0);
    }


}
