package com.foro_hub.api.topico;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoDTO(
        Long id,
        @NotBlank
        String titulo,
        @NotBlank
        String mensaje,
        @NotNull
        @JsonAlias("fecha_creacion") String fechaCreacion,
        @NotNull
        int status,
        @NotBlank
        String autor,
        @NotBlank
        String curso,
        String respuestas) {
}