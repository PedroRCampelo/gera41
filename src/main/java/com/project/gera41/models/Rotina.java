package com.project.gera41.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Código é obrigatório")
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    private Boolean ativo = true;

    @Lob
    private String principaisObjetivos;

    @Lob
    private String origensDados;

    @Lob
    private String fatoresCriticosSucesso;

    @Lob
    private String restricoes;
}