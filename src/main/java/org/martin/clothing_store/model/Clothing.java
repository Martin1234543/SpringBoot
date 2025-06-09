package org.martin.clothing_store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "clothing")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clothing {
    @Id
    @Column(nullable = false, unique = true)
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price", columnDefinition = "NUMERIC")
    private BigDecimal price;
    @Column(name = "is_active")
    private boolean isActive;
}
