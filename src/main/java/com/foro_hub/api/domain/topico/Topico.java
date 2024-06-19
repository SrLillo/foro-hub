package com.foro_hub.api.domain.topico;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "Topico")
@Table(name = "topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    String titulo;
    @NotBlank
    String mensaje;
    @NotNull
    String fechaCreacion;
    @NotNull
    int status;
    @NotBlank
    String autor;
    @NotBlank
    String curso;
    //@Embedded
    String respuestas;

    public Topico(TopicoDTO datosTopico) {
        this.titulo = datosTopico.titulo();
        this.mensaje = datosTopico.mensaje();
        this.fechaCreacion = datosTopico.fechaCreacion();
        this.status = 1;
        this.autor = datosTopico.autor();
        this.curso = datosTopico.curso();
        this.respuestas = datosTopico.respuestas();
    }
}