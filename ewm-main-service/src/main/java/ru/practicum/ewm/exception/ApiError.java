package ru.practicum.ewm.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    StackTraceElement[] errors; // Список стектрейсов или описания ошибок
    String message; //Сообщение об ошибке
    String reason; //Общее описание причины ошибки
    HttpStatus status; //Код статуса HTTP-ответа
    final LocalDateTime timestamp = LocalDateTime.now(); // Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
}
