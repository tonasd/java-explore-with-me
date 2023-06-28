package ru.practicum.ewm.dto.compilation;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class NewCompilationDto {
    boolean pinned;

    @NotBlank
    @Size(max = 50)
    String title;

    @UniqueElements
    List<Long> events = new ArrayList<>();
}
