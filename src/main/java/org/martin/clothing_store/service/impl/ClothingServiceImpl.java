package org.martin.clothing_store.service.impl;

import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.service.ClothingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ClothingServiceImpl implements ClothingService {
    ClothingRepository clothingRepository;
   @Autowired
   public ClothingServiceImpl(ClothingRepository clothingRepository) {
       this.clothingRepository = clothingRepository;
   }
    @Override
    public List<Clothing> findAll() {
        return clothingRepository.findAll();
    }

    @Override
    public List<Clothing> findAllAvailable() {
        return clothingRepository.findAvailableClothing();
    }

    @Override
    public Optional<Clothing> findById(String id) {
        return clothingRepository.findById(id);
    }

    @Override
    public Optional<Clothing> findByName(String name) {
        return clothingRepository.findByName(name);
    }

    @Override
    public Clothing save(Clothing clothing) {
        return clothingRepository.save(clothing);
    }

    @Override
    public List<Clothing> findQuantityIsNull() {
        return clothingRepository.findAll();//TODO
    }

    @Override
    public boolean isAvailable(String clothingId) {
        return clothingRepository.findById(clothingId).isPresent();
    }

    @Override
    public void deleteById(String id) {
        Optional<Clothing> clothing= clothingRepository.findById(id);
        if(clothing.isPresent()){
            clothing.get().setActive(false);
        }else {
            System.out.println("Clothing not found");
        }
    }
}
