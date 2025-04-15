package com.project.gera41.services;

import com.project.gera41.models.Documento;
import com.project.gera41.models.Rotina;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InMemoryDataService {
    private final List<Rotina> rotinas = new ArrayList<>();
    private final List<Documento> documentos = new ArrayList<>();
    private long rotinaIdCounter = 1;
    private long documentoIdCounter = 1;

    public InMemoryDataService() {
        // Inicializar com algumas rotinas de exemplo
        rotinas.add(createRotina("PC001", "Pedido de Compra", "Permite registrar solicitações de compra.", "ERP", true));
        rotinas.add(createRotina("FT001", "Faturamento", "Geração de notas fiscais.", "ERP", true));
        rotinas.add(createRotina("ES001", "Estoque", "Controle de entrada e saída de produtos.", "ERP", true));
    }

    private Rotina createRotina(String codigo, String nome, String descricao, String categoria, boolean ativo) {
        Rotina rotina = new Rotina();
        rotina.setId(rotinaIdCounter++);
        rotina.setCodigo(codigo);
        rotina.setNome(nome);
        rotina.setDescricao(descricao);
        rotina.setCategoria(categoria);
        rotina.setAtivo(ativo);
        return rotina;
    }

    // Métodos para Rotina
    public List<Rotina> findAllRotinas() {
        return new ArrayList<>(rotinas);
    }

    public List<Rotina> findRotinasByAtivoTrue() {
        return rotinas.stream()
                .filter(Rotina::isAtivo)
                .collect(Collectors.toList());
    }

    public Optional<Rotina> findRotinaById(Long id) {
        return rotinas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    // Métodos para Documento
    public List<Documento> findAllDocumentos() {
        return new ArrayList<>(documentos);
    }

    public Optional<Documento> findDocumentoById(Long id) {
        return documentos.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    public Documento saveDocumento(Documento documento) {
        if (documento.getId() == null) {
            documento.setId(documentoIdCounter++);
            documentos.add(documento);
        } else {
            documentos.removeIf(d -> d.getId().equals(documento.getId()));
            documentos.add(documento);
        }
        return documento;
    }

    public void deleteDocumentoById(Long id) {
        documentos.removeIf(d -> d.getId().equals(id));
    }
}