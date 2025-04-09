package com.project.gera41.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Entidade Rotina
@Entity
@Table(name = "rotinas")
@Getter @Setter
public class Rotina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  
    private Long id;
    
    
    private String codigo;
   
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    
    private String categoria;
   
    private boolean ativo = true;

}
