package com.project.gera41.repositories;

import com.project.gera41.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    // Métodos personalizados se necessário
}