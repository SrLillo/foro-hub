package com.foro_hub.api.topico;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<TopicoDTO> listarTopicos() {
        List<Topico> topicos = topicoRepository.findAll();
        return topicos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<TopicoDTO> mostrarTopico(@PathVariable Long id) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        return topicoOptional.map(this::convertirADTO);
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
}