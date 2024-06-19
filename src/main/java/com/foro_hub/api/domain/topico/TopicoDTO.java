package com.foro_hub.api.domain.topico;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoDTO(
        Long id,
        @NotBlank(message = "Título es obligatorio")
        String titulo,
        @NotBlank(message = "Mensaje es obligatorio")
        String mensaje,
        @NotNull(message = "Fecha de Creación es obligatorio")
        @JsonAlias("fecha_creacion")
        String fechaCreacion,
        @NotNull(message = "Status es obligatorio")
        int status,
        @NotBlank(message = "Autor es obligatorio")
        String autor,
        @NotBlank(message = "Curso es obligatorio")
        String curso,
        String respuestas) {
}