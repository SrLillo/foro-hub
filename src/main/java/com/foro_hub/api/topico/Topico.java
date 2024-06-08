package com.foro_hub.api.topico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Topico")
@Table(name = "topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String titulo;
    String mensaje;
    String fechaCreacion;
    int status;
    String autor;
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