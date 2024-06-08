package com.foro_hub.api.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoDTO(
        @NotBlank
        Long id,
        @NotNull
        String titulo,
        @NotNull
        String mensaje,
        @NotNull
        String fechaCreacion,
        @NotNull
        int status,
        @NotNull
        String autor,
        @NotNull
        String curso,
        String respuestas) {
}
