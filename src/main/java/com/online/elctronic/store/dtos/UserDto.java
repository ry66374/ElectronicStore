package com.online.elctronic.store.dtos;

import com.online.elctronic.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
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
public class UserDto {

    private String userId;

    @Size(min = 3, max = 20, message = "Invalid Name !! Choose length between 3 to 15 Character")
    private String name;

    //@Email(message = "Invalid Mail")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid user email")
    @NotBlank(message = "Enter Email")
    private String email;

    @NotBlank(message = "Password is required !!")
    private String password;

    @Size(min = 4,max = 6,message = "Invalid Gender !!")
    private String gender;

    @NotBlank(message = "About is required !!")
    private String about;

    @ImageNameValid
    private String imageName;
}
