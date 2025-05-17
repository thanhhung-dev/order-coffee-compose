package com.example.OrderCoffeeBE.Entity.Request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Data
public class PostProductRequest {
    private int id;
    private String name;
    private String description;
    private Integer price;
    private Integer status;
    private Integer category_id;
}
