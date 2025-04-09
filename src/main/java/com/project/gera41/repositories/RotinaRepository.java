package com.project.gera41.repositories;
import com.project.gera41.models.Rotina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RotinaRepository extends JpaRepository<Rotina, Long> {
    List<Rotina> findByAtivoTrue();
    List<Rotina> findByCategoria(String categoria);
}