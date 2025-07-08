package org.martin.clothing_store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    @Id
    @Column(nullable = false, unique = true)
    private String id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "order_date")
    private String orderDate;
    @Column(name = "total_amount")
    private String totalAmount;
    @Column(name = "status")
    private String status;
    @Column(name = "clothingId")
    private String clothingId;
    @Column(name = "quantity")
    private  String quantity;
}
