package ru.practicum.ewm.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
/**
 * Dto for creation of new user
 */
public class NewUserRequest {
    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
