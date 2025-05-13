package com.martin.car_rent.dto;

import com.martin.car_rent.model.Rental;
import com.martin.car_rent.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class RentalRequest {
    public String vehicleId;
    public String userId;
}
