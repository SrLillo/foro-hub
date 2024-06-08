package com.foro_hub.api.topico;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record TopicoDTO(
        Long id,
        @NotNull
        String titulo,
        @NotNull
        String mensaje,
        @NotNull
        @JsonAlias("fecha_creacion") String fechaCreacion,
        @NotNull
        int status,
        @NotNull
        String autor,
        @NotNull
        String curso,
        String respuestas) {
}
