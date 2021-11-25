package com.space.demo.dto.input;

import lombok.Data;

@Data
public class CreateIssueInput {
    private String title;
    private String description;
}
