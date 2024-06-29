package com.foro_hub.api.domain.topico;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TopicoRepositoryTest {

    @Autowired
    private TopicoRepository topicoRepository;

    @Test
    @DisplayName("Retorna todos los tópicos ordenados por fecha de creación")
    void testFindAllByFechaCreacionAsc() {
        TopicoDTO topicoDTO1 = new TopicoDTO(1L, "Topico 1", "Mensaje 1", "2022-01-01", 1, "Autor 1", "Curso 1", "Respuestas 1");
        TopicoDTO topicoDTO2 = new TopicoDTO(2L, "Topico 2", "Mensaje 2", "2022-01-02", 1, "Autor 2", "Curso 2", "Respuestas 2");
        TopicoDTO topicoDTO3 = new TopicoDTO(3L, "Topico 3", "Mensaje 3", "2022-01-03", 1, "Autor 3", "Curso 3", "Respuestas 3");

        Topico topico1 = new Topico(topicoDTO1);
        Topico topico2 = new Topico(topicoDTO2);
        Topico topico3 = new Topico(topicoDTO3);

        topicoRepository.save(topico1);
        topicoRepository.save(topico2);
        topicoRepository.save(topico3);

        Iterable<Topico> topicos = topicoRepository.findAllByFechaCreacionAsc();

        int i = 1;
        for (Topico topico : topicos) {
            assertEquals("Topico " + i, topico.getTitulo());
            assertEquals("2022-01-0" + i, topico.getFechaCreacion());
            assertEquals("Autor " + i, topico.getAutor());
            assertEquals("Curso " + i, topico.getCurso());
            assertEquals("Mensaje " + i, topico.getMensaje());
            assertEquals("Respuestas " + i, topico.getRespuestas());
            i++;
        }
    }

    @Test
    public void testFindAllByFechaCreacionAsc_EmptyResult() {
        // Call the method under test
        List<Topico> result = topicoRepository.findAllByFechaCreacionAsc();

        // Verify the result
        assertEquals(0, result.size());
    }

}