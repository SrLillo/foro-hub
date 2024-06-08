package com.foro_hub.api.topico;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Topico> listarTopicos() {
        return topicoRepository.findAll();
    }
}