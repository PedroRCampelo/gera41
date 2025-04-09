package com.project.gera41.repositories;
import com.project.gera41.models.Rotina;
import java.util.List;

public interface RotinaRepository {
    List<Rotina> findAll();
    Rotina findById(Long id);
    void save(Rotina rotina);
    void update(Rotina rotina);
    void delete(Long id);
    List<Rotina> findByModulo(String modulo);
}