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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {

    private static final Logger LOGGER = Logger.getLogger(DocumentoController.class.getName());

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

    @GetMapping("/novo")
    public String novoDocumento(Model model) {
        model.addAttribute("documento", new Documento());
        model.addAttribute("rotinas", rotinaRepository.findByAtivoTrue());
        LOGGER.info("Acessando formulário de novo documento");
        return "documento/form";
    }

    @GetMapping("/listar")
    public String listarDocumentos(Model model) {
        List<Documento> documentos = documentoRepository.findAll();
        model.addAttribute("documentos", documentos);
        LOGGER.info("Listando documentos: " + documentos.size() + " encontrados");
        return "documento/lista";
    }

    @PostMapping("/salvar")
    public String salvarDocumento(
            @Valid @ModelAttribute Documento documento,
            BindingResult result,
            @RequestParam(value = "rotinasIds", required = false) List<Long> rotinasIds,
            @RequestParam(value = "ordens", required = false) List<Integer> ordens,
            Model model) {

        LOGGER.info("Tentando salvar documento: " + documento.getNomeProjeto());

        if (result.hasErrors()) {
            LOGGER.warning("Erros de validação encontrados: " + result.getAllErrors());
            model.addAttribute("rotinas", rotinaRepository.findByAtivoTrue());
            return "documento/form";
        }

        if (documento.getDocumentoRotinas() == null) {
            documento.setDocumentoRotinas(new ArrayList<>());
        } else {
            documento.getDocumentoRotinas().clear();
        }

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
                    LOGGER.info("Adicionada rotina ID " + rotinaId + " com ordem " + ordem);
                }
            }
        }

        documento.setDataCriacao(LocalDate.now()); // Adicionar data de criação
        documentoRepository.save(documento);
        LOGGER.info("Documento salvo com ID: " + documento.getId());
        return "redirect:/documentos/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarDocumento(@PathVariable Long id, Model model) {
        Optional<Documento> documentoOpt = documentoRepository.findById(id);
        if (documentoOpt.isPresent()) {
            model.addAttribute("documento", documentoOpt.get());
            model.addAttribute("rotinas", rotinaRepository.findByAtivoTrue());
            LOGGER.info("Editando documento ID: " + id);
            return "documento/form";
        }
        LOGGER.warning("Documento ID " + id + " não encontrado para edição");
        return "redirect:/documentos/listar";
    }

    @GetMapping("/gerar/{id}")
    public ResponseEntity<ByteArrayResource> gerarDocumento(@PathVariable Long id) {
        Optional<Documento> documentoOpt = documentoRepository.findById(id);

        if (!documentoOpt.isPresent()) {
            LOGGER.warning("Documento ID " + id + " não encontrado para geração");
            return ResponseEntity.notFound().build();
        }

        Documento documento = documentoOpt.get();

        try {
            File docFile = wordGeneratorService.generateDocument(documento, documento.getDocumentoRotinas());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(docFile.toPath()));

            docFile.deleteOnExit();

            String fileName = (documento.getCodigoCliente() != null ? documento.getCodigoCliente() : "cliente") + "_" +
                    (documento.getCodigoProjeto() != null ? documento.getCodigoProjeto() : "projeto") + ".docx";
            fileName = fileName.replaceAll("[^a-zA-Z0-9_-]", "_");

            LOGGER.info("Documento Word gerado para ID: " + id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(docFile.length())
                    .body(resource);
        } catch (IOException e) {
            LOGGER.severe("Erro ao gerar documento Word para ID " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirDocumento(@PathVariable Long id) {
        documentoRepository.deleteById(id);
        LOGGER.info("Documento ID " + id + " excluído");
        return "redirect:/documentos/listar";
    }
}