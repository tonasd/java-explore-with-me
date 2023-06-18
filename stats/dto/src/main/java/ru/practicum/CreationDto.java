package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.net.InetAddress;
import java.net.URI;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationDto {
    @NotNull String app;
    @NotNull URI uri;
    @NotNull InetAddress ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent LocalDateTime timestamp;
}
