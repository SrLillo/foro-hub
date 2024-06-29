package com.foro_hub.api.controller;

import com.foro_hub.api.domain.topico.Topico;
import com.foro_hub.api.domain.topico.TopicoDTO;
import com.foro_hub.api.domain.topico.TopicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicoControllerTest {

    @Mock
    private TopicoRepository topicoRepository;

    @InjectMocks
    private TopicoController topicoController;

    @Test
    void registrarTopico() {
        TopicoDTO topicoDTO = new TopicoDTO(1L, "titulo", "mensaje", "fechaCreacion", 1, "autor", "curso", "respuestas");
        Topico topico = new Topico(topicoDTO);
        when(topicoRepository.save(topico)).thenReturn(topico);

        ResponseEntity<TopicoDTO> response = topicoController.registrarTopico(topicoDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void listarTopicos() {
        List<Topico> topicos = new ArrayList<>();
        topicos.add(new Topico(new TopicoDTO(1L, "titulo1", "mensaje1", "fechaCreacion1", 1, "autor1", "curso1", "respuestas1")));
        topicos.add(new Topico(new TopicoDTO(2L, "titulo2", "mensaje2", "fechaCreacion2", 1, "autor2", "curso2", "respuestas2")));

        PageRequest pageable = PageRequest.of(0, 5);
        Page<Topico> page = new PageImpl<>(topicos, pageable, topicos.size());

        when(topicoRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<TopicoDTO>> response = topicoController.listarTopicos(pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void mostrarTopico() {
        Topico topico = new Topico(new TopicoDTO(1L, "titulo", "mensaje", "fechaCreacion", 1, "autor", "curso", "respuestas"));
        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topico));

        ResponseEntity<TopicoDTO> response = topicoController.mostrarTopico(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void actualizarTopico() {
        Topico topico = new Topico(new TopicoDTO(1L, "titulo", "mensaje", "fechaCreacion", 1, "autor", "curso", "respuestas"));
        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topico));
        when(topicoRepository.save(topico)).thenReturn(topico);

        ResponseEntity<Optional<TopicoDTO>> response = topicoController.actualizarTopico(1L, new TopicoDTO(1L, "titulo", "mensaje", "fechaCreacion", 1, "autor", "curso", "respuestas"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void eliminarTopico() {
        when(topicoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(topicoRepository).deleteById(1L);

        ResponseEntity response = topicoController.eliminarTopico(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}