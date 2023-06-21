package ru.practicum.ewm.stats.client;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.CreationDto;

import java.net.InetAddress;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatsClientTest {

    @Mock
    private RestTemplate restTemplate;

    private StatsClient client;

    private CreationDto dto;

    @BeforeEach
    @SneakyThrows
    void beforeEach() {
        client = new StatsClient("", new RestTemplateBuilder());
        ReflectionTestUtils.setField(client, "rest", restTemplate);

        dto = new CreationDto();
        dto.setUri(URI.create("/test"));
        dto.setIp(InetAddress.getByName("255.255.255.0"));
        dto.setApp("my-app");
        dto.setTimestamp(LocalDateTime.now());
    }

    @Test
    void saveRequest_shouldReturnCreated_whenInvoked() {

        Mockito.when(restTemplate.postForEntity("/hit", dto, void.class))
                .thenReturn(new ResponseEntity(HttpStatus.CREATED));


        assertDoesNotThrow(() -> client.saveRequest(dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp()));
        Mockito.verifyNoMoreInteractions(restTemplate);
    }
}
