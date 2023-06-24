package ru.practicum.ewm.dto.user;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
/**
 * Dto for creation of new user
 */
public class NewUserRequest {
    @NotNull @Email
    private String email;

    @NotNull @Size(min = 2, max = 250)
    private String name;
}
