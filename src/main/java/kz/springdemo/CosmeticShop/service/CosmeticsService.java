package kz.springdemo.CosmeticShop.service;

import kz.springdemo.CosmeticShop.entities.Cosmetics;

import java.util.List;
import java.util.Optional;

public interface CosmeticsService {
    Cosmetics addCosmetic(Cosmetics cosmetic);
    List<Cosmetics> getAllCosmetics(String keyword);
    Cosmetics getCosmetic(Long id);
    void deleteCosmeticById(Long id);
    void deleteCosmetic(Cosmetics cosmetics);
    Cosmetics saveCosmetic(Cosmetics cosmetic);
    List<Cosmetics> getAllCosmeticsByCategoryId(Long id);
    List<Cosmetics>getAllCosmeticsByTypeId(Long id);
  Cosmetics getCosmeticsById(Long id);
  List<Cosmetics>getAllCosmetic();
  List<Cosmetics>getAllProduct();


}
