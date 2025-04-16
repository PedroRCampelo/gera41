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
        rotina.setCodigo("CDFT001");
        rotina.setNome("Clientes");
        rotina.setDescricao("Cliente é a entidade que possui necessidades de produtos e serviços a serem supridas pelas empresas.\n" +
                "Conceitualmente, na maioria das vezes em que é emitido um documento de saída, o destinatário é considerado um cliente, independentemente do tipo que ele possua ou da denominação que a empresa tenha determinado para ele.\n" +
                "\n" +
                "No ambiente FATURAMENTO, o cadastro de Clientes é uma etapa obrigatória para que os pedidos de vendas sejam registrados e os documentos de saída possam ser gerados. É possível classificar os clientes em cinco tipos:\n");
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