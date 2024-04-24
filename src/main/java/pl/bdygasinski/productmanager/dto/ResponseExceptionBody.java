package pl.bdygasinski.productmanager.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseExceptionBody(LocalDateTime timestamp, String message) {
}
