package com.foro_hub.api.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    @Query("""
            SELECT t FROM Topico t 
            ORDER BY FUNCTION('STR_TO_DATE', t.fechaCreacion, '%m/%d/%Y') ASC
            """)
    List<Topico> findAllByFechaCreacionAsc();
}