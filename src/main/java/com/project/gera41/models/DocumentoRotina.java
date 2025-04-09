package com.project.gera41.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Entidade de relacionamento
@Entity
@Table(name = "documento_rotinas")
@Getter @Setter
public class DocumentoRotina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "documento_id")
    private Documento documento;

    @ManyToOne
    @JoinColumn(name = "rotina_id")
    private Rotina rotina;
    
    private int ordem;
    
}
