package com.electonic.store.ElectonicStore.dtos;

import com.electonic.store.ElectonicStore.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {

    private String id;
    @Size(min = 3, max = 50, message = "Invalid name !!")
    private String name;
//    @Email(message = "Invalid Email address !!")
    @Pattern(regexp = "^[\\w\\.-]+@([\\da-zA-Z-]+\\.)+[a-zA-Z]{2,}$", message = "Invalid Email address !!")
    @NotBlank(message = "Email is required !!")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "Enter valid Password !!")
    @NotBlank(message = "Password is required !!")
    private String password;
    @Size(min = 4, max = 6, message = "Invalid gender !!")
    private String gender;
    @NotBlank(message = "Write something about you !!")
    private String about;
    @ImageNameValid
    private String imageName;
}
