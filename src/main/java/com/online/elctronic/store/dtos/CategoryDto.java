package com.online.elctronic.store.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {


    private String categoryId;

    @NotBlank(message = "Value is required")
    @Size(min = 4, message = "Title must be minimum of 4 Characters !!")
    private String title;

    @NotBlank(message = "Description required !!")
    private String description;

    private String coverImage;

}
