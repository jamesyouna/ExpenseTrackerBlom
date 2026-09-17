package com.BLOM.expensetrackerapi.io;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    private String name;
    private String description;
    private String icon; //s3 bucket to store icons
}
