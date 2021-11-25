package com.space.demo.dto.input;

import com.space.demo.dto.enumeration.CategoryEnum;
import lombok.Data;

@Data
public class CreateTaskInput {
    private String title;
    private CategoryEnum category;
}
