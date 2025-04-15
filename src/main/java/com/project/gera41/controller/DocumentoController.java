package com.project.gera41.controller;

import com.project.gera41.models.Documento;
import com.project.gera41.models.DocumentoRotina;
import com.project.gera41.models.Rotina;
import com.project.gera41.repositories.DocumentoRepository;
import com.project.gera41.repositories.RotinaRepository;
import com.project.gera41.services.WordGeneratorService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {

    private final RotinaRepository rotinaRepository;
    private final DocumentoRepository documentoRepository;
    private final WordGeneratorService wordGeneratorService;

    public DocumentoController(RotinaRepository rotinaRepository,
                               DocumentoRepository documentoRepository,
                               WordGeneratorService wordGeneratorService) {
        this.rotinaRepository = rotinaRepository;
        this.documentoRepository = documentoRepository;
        this.wordGeneratorService = wordGeneratorService;
    }

    /**
     * Exibe o formulário para criar um novo documento
     */
    @GetMapping("/novo")
    public String novoDocumento(Model model) {
        model.addAttribute("documento", new Documento());
        model.addAttribute("rotinas", rotinaRepository.findByAtivoTrue());
        return "documento/form";
    }

    /**
     * Lista todos os documentos
     */
    @GetMapping("/listar")
    public String listarDocumentos(Model model) {
        model.addAttribute("documentos", documentoRepository.findAll());
        return "documento/lista";
    }

    /**
     * Salva um documento com suas rotinas selecionadas
     */
    @PostMapping("/salvar")
    public String salvarDocumento(
            @Valid @ModelAttribute Documento documento,
            BindingResult result,
            @RequestParam(value = "rotinasIds", required = false) List<Long> rotinasIds,
            @RequestParam(value = "ordens", required = false) List<Integer> ordens,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("rotinas", rotinaRepository.findByAtivoTrue());
            return "documento/form";
        }

        // Inicializar a lista de DocumentoRotina se for null
        if (documento.getDocumentoRotinas() == null) {
            documento.setDocumentoRotinas(new ArrayList<>());
        } else {
            documento.getDocumentoRotinas().clear(); // Limpar rotinas existentes para atualizar
        }

        // Associar as rotinas selecionadas ao documento
        if (rotinasIds != null && ordens != null && rotinasIds.size() == ordens.size()) {
            for (int i = 0; i < rotinasIds.size(); i++) {
                Long rotinaId = rotinasIds.get(i);
                int ordem = ordens.get(i);

                Optional<Rotina> rotinaOpt = rotinaRepository.findById(rotinaId);
                if (rotinaOpt.isPresent()) {
                    DocumentoRotina docRotina = new DocumentoRotina();
                    docRotina.setDocumento(documento);
                    docRotina.setRotina(rotinaOpt.get());
                    docRotina.setOrdem(ordem);
                    documento.getDocumentoRotinas().add(docRotina);
                }
            }
        }

        documentoRepository.save(documento);
        return "redirect:/documentos/listar";
    }

    /**
     * Exibe o formulário para editar um documento existente
     */
    @GetMapping("/editar/{id}")
    public String editarDocumento(@PathVariable Long id, Model model) {
        Optional<Documento> documentoOpt = documentoRepository.findById(id);
        if (documentoOpt.isPresent()) {
            model.addAttribute("documento", documentoOpt.get());
            model.addAttribute("rotinas", rotinaRepository.findByAtivoTrue());
            return "documento/form";
        }
        return "redirect:/documentos/listar";
    }

    /**
     * Gera e faz download do documento Word
     */
    @GetMapping("/gerar/{id}")
    public ResponseEntity<ByteArrayResource> gerarDocumento(@PathVariable Long id) {
        Optional<Documento> documentoOpt = documentoRepository.findById(id);

        if (!documentoOpt.isPresent()) {
            return ResponseEntity.<ByteArrayResource>notFound().build();
        }

        Documento documento = documentoOpt.get();

        try {
            File docFile = wordGeneratorService.generateDocument(documento, documento.getDocumentoRotinas());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(docFile.toPath()));

            // Limpar o arquivo temporário após leitura
            docFile.deleteOnExit();

            // Sanitizar o nome do arquivo
            String fileName = (documento.getCodigoCliente() != null ? documento.getCodigoCliente() : "cliente") + "_" +
                    (documento.getCodigoProjeto() != null ? documento.getCodigoProjeto() : "projeto") + ".docx";
            fileName = fileName.replaceAll("[^a-zA-Z0-9_-]", "_");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(docFile.length())
                    .body(resource);
        } catch (IOException e) {
            // Log do erro (você pode usar um logger aqui)
            return ResponseEntity.<ByteArrayResource>internalServerError().build();
        }
    }

    /**
     * Exclui um documento
     */
    @GetMapping("/excluir/{id}")
    public String excluirDocumento(@PathVariable Long id) {
        documentoRepository.deleteById(id);
        return "redirect:/documentos/listar";
    }
}