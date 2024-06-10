package com.foro_hub.api.topico;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    public void registrarTopico(@RequestBody @Valid TopicoDTO datosTopico) {
        topicoRepository.save(new Topico(datosTopico));
    }

    @GetMapping
    public Page<TopicoDTO> listarTopicos(@PageableDefault(size = 5, page = 0, direction = ASC) Pageable paginacion) {
        return buscarTopicos(paginacion, null);
    }

    @GetMapping("/titulo")
    public Page<TopicoDTO> listarTopicosPorTitulo(@PageableDefault(size = 5, page = 0, direction = ASC, sort = {"titulo"}) Pageable paginacion) {
        return buscarTopicos(paginacion, "titulo");
    }

    @GetMapping("/curso")
    public Page<TopicoDTO> listarTopicosPorCurso(@PageableDefault(size = 5, page = 0, direction = ASC, sort = {"curso"}) Pageable paginacion) {
        return buscarTopicos(paginacion, "curso");
    }

    @GetMapping("/fecha")
    public Page<TopicoDTO> listarTopicosPorFecha(@PageableDefault(size = 5, page = 0, direction = ASC, sort = {"fechaCreacion"}) Pageable paginacion) {
        return buscarTopicos(paginacion, "fechaCreacion");
    }

    @GetMapping("/anio")
    public Page<TopicoDTO> listarTopicosPorAnio(@PageableDefault(size = 5, page = 0, direction = ASC) Pageable paginacion) {
        return buscarTopicosPorAnio(paginacion);
    }

    @GetMapping("/{id}")
    public Optional<TopicoDTO> mostrarTopico(@PathVariable Long id) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        return topicoOptional.map(this::convertirADTO);
    }

    @PutMapping("/{id}")
    @Transactional
    public Optional<TopicoDTO> actualizarTopico(@PathVariable Long id, @RequestBody @Valid TopicoDTO topicoDTO) {
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
            return Optional.of(convertirADTO(topico));
        } else {
            return Optional.empty();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarTopico(@PathVariable Long id) {
        if (topicoRepository.existsById(id)) {
            topicoRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topico no encontrado");
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