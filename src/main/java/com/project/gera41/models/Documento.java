package com.project.gera41.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

// Entidade Documento
@Entity
@Table(name = "documentos")
@Getter @Setter
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    
    private String nomeCliente;
    private String codigoCliente;
    private String nomeProjeto;
    private String codigoProjeto;
    private LocalDate dataCriacao;
    private String segmentoCliente;
    private String unidadeTotvs;
    private String propostaComercial;
    private String gerenteTotvs;
    private String gerenteCliente;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentoRotina> documentoRotinas = new ArrayList<>();
    
}