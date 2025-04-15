package com.project.gera41.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "documentos")
@Getter
@Setter
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do documento é obrigatório")
    private String nome;

    @NotBlank(message = "Nome do cliente é obrigatório")
    private String nomeCliente;

    @NotBlank(message = "Código do cliente é obrigatório")
    private String codigoCliente;

    @NotBlank(message = "Nome do projeto é obrigatório")
    private String nomeProjeto;

    @NotBlank(message = "Código do projeto é obrigatório")
    private String codigoProjeto;

    private LocalDate dataCriacao;

    private String segmentoCliente;
    private String unidadeTotvs;
    private String propostaComercial;
    private String gerenteTotvs;
    private String gerenteCliente;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentoRotina> documentoRotinas = new ArrayList<>();
}