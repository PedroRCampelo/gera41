package com.project.gera41.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rotinas")
@Getter
@Setter
public class Rotina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private String nome;

    @Column(length=1000000)
    private String descricao;

    private String categoria;

    private Boolean ativo = true;
}