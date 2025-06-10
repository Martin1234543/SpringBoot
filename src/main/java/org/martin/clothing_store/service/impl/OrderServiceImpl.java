package org.martin.clothing_store.service.impl;

import org.martin.clothing_store.model.Cart;
import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.model.Orders;
import org.martin.clothing_store.repository.CartRepository;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.repository.OrderRepository;
import org.martin.clothing_store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    ClothingRepository  clothingRepository;
    OrderRepository orderRepository;
    CartRepository cartRepository;
    @Autowired
    public OrderServiceImpl(CartRepository cartRepository, ClothingRepository clothingRepository, OrderRepository orderRepository) {
        this.clothingRepository = clothingRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;

    }
    @Override
    public boolean isClothingAvailable(String clothingId) {
        return clothingRepository.findById(clothingId).isPresent();
    }
    private Map<String, Integer> parseItems(String itemsId) {
        Map<String, Integer> map = new HashMap<>();
        if (itemsId == null || itemsId.isBlank()) return map;

        for (String pair : itemsId.split(",")) {
            String[] parts = pair.split(":");
            if (parts.length == 2) {
                try {
                    map.put(parts[0], Integer.parseInt(parts[1]));
                } catch (NumberFormatException ignored) {}
            }
        }
        return map;
    }

    @Override
    public Orders buy(String userId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) return null;

        Cart cart = cartOpt.get();
        Map<String, Integer> itemMap = parseItems(cart.getItemsId());
        if (itemMap.isEmpty()) return null;

        List<String> boughtIds = new ArrayList<>();
        double totalAmount = 0;

        for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
            String clothingId = entry.getKey();
            int quantity = entry.getValue();

            Optional<Clothing> clothingOpt = clothingRepository.findById(clothingId);
            if (clothingOpt.isPresent()) {
                Clothing clothing = clothingOpt.get();

                if (clothing.getQuantity() >= quantity && clothing.isActive()) {
                    clothing.setQuantity(clothing.getQuantity() - quantity);
                    clothingRepository.save(clothing);

                    totalAmount += clothing.getPrice().doubleValue() * quantity;
                    boughtIds.add(clothingId);
                }
            }
        }

        if (boughtIds.isEmpty()) return null;

        Orders order = new Orders();
        order.setId(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now().toString());
        order.setStatus("bought");
        order.setTotalAmount(String.valueOf(totalAmount));
        order.setClothingId(String.join(",", boughtIds)); // tu zapisujemy ID

        orderRepository.save(order);

        // czyścimy koszyk
        cart.setItemsId(null);
        cartRepository.save(cart);

        return order;
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
