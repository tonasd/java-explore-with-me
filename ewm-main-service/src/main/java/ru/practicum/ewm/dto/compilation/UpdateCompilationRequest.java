package ru.practicum.ewm.dto.compilation;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateCompilationRequest {
    Boolean pinned;

    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "title must not be blank(have only spaces)")
    @Size(min = 1, max = 50)
    String title;

    @UniqueElements
    List<Long> events;
}
