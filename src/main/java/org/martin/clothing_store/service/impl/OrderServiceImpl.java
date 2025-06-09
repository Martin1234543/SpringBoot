package org.martin.clothing_store.service.impl;

import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.model.Orders;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.repository.OrderRepository;
import org.martin.clothing_store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    ClothingRepository  clothingRepository;
    OrderRepository orderRepository;
    @Autowired
    public OrderServiceImpl(ClothingRepository clothingRepository, OrderRepository orderRepository) {
        this.clothingRepository = clothingRepository;
        this.orderRepository = orderRepository;

    }
    @Override
    public boolean isClothingAvailable(String clothingId) {
        return clothingRepository.findById(clothingId).isPresent();
    }

    @Override
    public Orders buy(String clothingId, String userId, int quantity) {
        Optional<Clothing> clothing = clothingRepository.findById(clothingId);
        if(clothing.isPresent()&&clothing.get().getQuantity()>quantity&&clothing.get().isActive()) {
            clothing.get().setQuantity(clothing.get().getQuantity()-quantity);
            clothingRepository.save(clothing.get());
            Orders orders = new Orders();
            orders.setId(UUID.randomUUID().toString());
            orders.setClothingId(clothingId);
            orders.setUserId(userId);
            orders.setStatus("bought");
            orders.setTotalAmount(String.valueOf(quantity));
            orders.setOrderDate(LocalDateTime.now().toString());
            orderRepository.save(orders);
            return orders;
        }
        System.out.println("Error buying clothing");
        return null;
    }

    @Override
    public Orders returnClothing(String clothingId, String userId) {

        Optional<Orders> orders = orderRepository.findById(clothingId);
        if(orders.isPresent()&&orders.get().getStatus().equals("bought")&&orders.get().getUserId().equals(userId)) {
            Optional<Clothing> clothing = clothingRepository.findById(clothingId);
            if (clothing.isPresent()) {
                clothing.get().setQuantity(clothing.get().getQuantity()+Integer.parseInt(orders.get().getTotalAmount()));
                clothingRepository.save(clothing.get());
                orders.get().setStatus("returned");
                orderRepository.save(orders.get());
                return orders.get();
            }
        }
        return null;
    }

    @Override
    public List<Orders> findAll() {
        return orderRepository.findAll();
    }
}
