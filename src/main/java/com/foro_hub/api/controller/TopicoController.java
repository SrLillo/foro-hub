package com.foro_hub.api.controller;

import com.foro_hub.api.domain.topico.Topico;
import com.foro_hub.api.domain.topico.TopicoDTO;
import com.foro_hub.api.domain.topico.TopicoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {
    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity<TopicoDTO> registrarTopico(@RequestBody @Valid TopicoDTO topicoDTO) {
        Topico topico = new Topico(topicoDTO);
        Topico topicoGuardado = topicoRepository.save(topico);
        TopicoDTO topicoGuardadoDTO = convertirADTO(topicoGuardado);
        URI location = URI.create("/topicos/" + topicoGuardadoDTO.id());
        return ResponseEntity.created(location).body(topicoGuardadoDTO);
    }

    @GetMapping
    public ResponseEntity<Page<TopicoDTO>> listarTopicos(@PageableDefault(size = 5, page = 0, direction = ASC) Pageable paginacion) {
        Page<TopicoDTO> topicos = buscarTopicos(paginacion, null);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/titulo")
    public ResponseEntity<Page<TopicoDTO>> listarTopicosPorTitulo(@PageableDefault(size = 5, page = 0, direction = ASC, sort = {"titulo"}) Pageable paginacion) {
        Page<TopicoDTO> topicos = buscarTopicos(paginacion, "titulo");
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/curso")
    public ResponseEntity<Page<TopicoDTO>> listarTopicosPorCurso(@PageableDefault(size = 5, page = 0, direction = ASC, sort = {"curso"}) Pageable paginacion) {
        Page<TopicoDTO> topicos = buscarTopicos(paginacion, "curso");
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/fecha")
    public ResponseEntity<Page<TopicoDTO>> listarTopicosPorFecha(@PageableDefault(size = 5, page = 0, direction = ASC, sort = {"fechaCreacion"}) Pageable paginacion) {
        Page<TopicoDTO> topicos = buscarTopicos(paginacion, "fechaCreacion");
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/anio")
    public ResponseEntity<Page<TopicoDTO>> listarTopicosPorAnio(@PageableDefault(size = 5, page = 0, direction = ASC) Pageable paginacion) {
        Page<TopicoDTO> topicos = buscarTopicosPorAnio(paginacion);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoDTO> mostrarTopico(@PathVariable Long id) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        return topicoOptional.map(topico -> ResponseEntity.ok(convertirADTO(topico)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Optional<TopicoDTO>> actualizarTopico(@PathVariable Long id, @RequestBody @Valid TopicoDTO topicoDTO) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isPresent()) {
            Topico topico = topicoOptional.get();
            topico.setTitulo(topicoDTO.titulo());
            topico.setMensaje(topicoDTO.mensaje());
            topico.setFechaCreacion(topicoDTO.fechaCreacion());
            topico.setStatus(topicoDTO.status());
            topico.setAutor(topicoDTO.autor());
            topico.setCurso(topicoDTO.curso());
            topico.setRespuestas(topicoDTO.respuestas());
            topicoRepository.save(topico);
            return ResponseEntity.ok(Optional.of(convertirADTO(topico)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        if (topicoRepository.existsById(id)) {
            topicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topico no encontrado");
            return ResponseEntity.notFound().build();
        }
    }

    private TopicoDTO convertirADTO(Topico topico) {
        return new TopicoDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                topico.getCurso(),
                topico.getRespuestas()
        );
    }

    private Page<TopicoDTO> buscarTopicos(Pageable paginacion, String sortBy) {
        if (sortBy != null) {
            paginacion = PageRequest.of(paginacion.getPageNumber(), paginacion.getPageSize(), ASC, sortBy);
        }
        return topicoRepository.findAll(paginacion).map(this::convertirADTO);
    }

    private Page<TopicoDTO> buscarTopicosPorAnio(Pageable paginacion) {
        List<Topico> topicos = topicoRepository.findAll();
        topicos.sort((t1, t2) -> {
            String[] fecha1 = t1.getFechaCreacion().split("/");
            String[] fecha2 = t2.getFechaCreacion().split("/");
            return Integer.compare(Integer.parseInt(fecha1[2]), Integer.parseInt(fecha2[2]));
        });
        return new PageImpl<>(topicos.stream().map(this::convertirADTO).collect(Collectors.toList()));
    }
}