package com.space.demo.dto;

import com.space.demo.dto.enumeration.CategoryEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Card {
    private String title;
    private String description;
    private List<CategoryEnum> categories;
}
