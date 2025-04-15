package com.project.gera41.services;

import com.project.gera41.models.Documento;
import com.project.gera41.models.Rotina;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryDataService {

    private final List<Rotina> rotinas = new ArrayList<>();
    private final List<Documento> documentos = new ArrayList<>();
    private final AtomicLong rotinaIdCounter = new AtomicLong(1);
    private final AtomicLong documentoIdCounter = new AtomicLong(1);

    public InMemoryDataService() {
        // Inicializar rotinas
        Rotina rotina = new Rotina();
        rotina.setId(rotinaIdCounter.getAndIncrement());
        rotina.setCodigo("ROT001");
        rotina.setNome("Rotina de Cadastro");
        rotina.setDescricao("Gerenciamento de cadastros");
        rotina.setCategoria("Cadastro");
        rotina.setAtivo(true);
        rotinas.add(rotina);
    }

    public List<Rotina> getAllRotinas() {
        return rotinas;
    }

    public List<Rotina> getActiveRotinas() {
        List<Rotina> activeRotinas = new ArrayList<>();
        for (Rotina r : rotinas) {
            if (r.getAtivo()) {
                activeRotinas.add(r);
            }
        }
        return activeRotinas;
    }

    public Optional<Rotina> getRotinaById(Long id) {
        for (Rotina r : rotinas) {
            if (r.getId().equals(id)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public List<Documento> getAllDocumentos() {
        return documentos;
    }

    public Optional<Documento> getDocumentoById(Long id) {
        for (Documento d : documentos) {
            if (d.getId().equals(id)) {
                return Optional.of(d);
            }
        }
        return Optional.empty();
    }

    public Documento saveDocumento(Documento documento) {
        if (documento.getId() == null) {
            documento.setId(documentoIdCounter.getAndIncrement());
        } else {
            documentos.removeIf(d -> d.getId().equals(documento.getId()));
        }
        documentos.add(documento);
        return documento;
    }

    public void deleteDocumento(Long id) {
        documentos.removeIf(d -> d.getId().equals(id));
    }
}