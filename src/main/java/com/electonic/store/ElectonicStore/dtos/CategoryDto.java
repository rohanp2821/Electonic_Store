package com.electonic.store.ElectonicStore.dtos;


import com.electonic.store.ElectonicStore.validate.ImageNameValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;
    @NotBlank
    @Size(min = 2, message = "Title must be have minimum 3 characters !!")
    private String title;
    @NotBlank (message = "Description is required !!")
    @Size(min = 3, message = "Description must be have minimum 3 characters !!")
    private String description;
    @NotBlank(message = "Cover Image is required !!")
    @ImageNameValid(message = "Use image format !!")
    private String coverImage;

}
