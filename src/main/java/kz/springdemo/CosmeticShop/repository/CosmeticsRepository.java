package kz.springdemo.CosmeticShop.repository;

import kz.springdemo.CosmeticShop.entities.Cosmetics;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CosmeticsRepository extends JpaRepository<Cosmetics,Long> {
    List<Cosmetics>findAllByCategoryId(Long id);
    List<Cosmetics>findAllByTypeId(Long id);

    ////////////////////////////////////////
    List<Cosmetics>findAllByAmountGreaterThanOrderByPriceDesc(int amount);
    List<Cosmetics>findAllByAmountGreaterThanOrderByPriceAsc(int amount);

    @Query("select c FROM Cosmetics c where c.name like %?1% or c.description like %?1% or c.category like %?1% or c.price like %?1% or c.amount like %?1%")

    List<Cosmetics>findAll(String keyword);




}
