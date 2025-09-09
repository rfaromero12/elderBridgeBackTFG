package es.uco.tfg.elderBridge.infrastructure.rest.dtos.event;

import jakarta.validation.constraints.NotBlank;

public record ShowSocietyEventDTO(@NotBlank String name) {

}
