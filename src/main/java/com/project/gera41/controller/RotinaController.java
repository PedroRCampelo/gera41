package com.project.gera41.controller;

import com.project.gera41.models.Rotina;
import com.project.gera41.repositories.RotinaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/rotinas")
public class RotinaController {

    private static final Logger LOGGER = Logger.getLogger(RotinaController.class.getName());

    private final RotinaRepository rotinaRepository;

    public RotinaController(RotinaRepository rotinaRepository) {
        this.rotinaRepository = rotinaRepository;
    }

    @GetMapping("/listar")
    public String listarRotinas(Model model) {
        List<Rotina> rotinas = rotinaRepository.findAll();
        model.addAttribute("rotinas", rotinas);
        LOGGER.info("Listando rotinas: " + rotinas.size() + " encontradas");
        return "rotina/lista";
    }

    @GetMapping("/novo")
    public String novoRotina(Model model) {
        model.addAttribute("rotina", new Rotina());
        LOGGER.info("Acessando formulário de nova rotina");
        return "rotina/form";
    }

    @GetMapping("/editar/{id}")
    public String editarRotina(@PathVariable Long id, Model model) {
        Optional<Rotina> rotinaOpt = rotinaRepository.findById(id);
        if (rotinaOpt.isPresent()) {
            model.addAttribute("rotina", rotinaOpt.get());
            LOGGER.info("Editando rotina ID: " + id);
            return "rotina/form";
        }
        LOGGER.warning("Rotina ID " + id + " não encontrada para edição");
        return "redirect:/rotinas/listar";
    }

    @PostMapping("/salvar")
    public String salvarRotina(@Valid @ModelAttribute Rotina rotina, BindingResult result, Model model) {
        LOGGER.info("Tentando salvar rotina: " + rotina.getNome());

        if (result.hasErrors()) {
            LOGGER.warning("Erros de validação encontrados: " + result.getAllErrors());
            return "rotina/form";
        }

        rotinaRepository.save(rotina);
        LOGGER.info("Rotina salva com ID: " + rotina.getId());
        return "redirect:/rotinas/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluirRotina(@PathVariable Long id) {
        rotinaRepository.deleteById(id);
        LOGGER.info("Rotina ID " + id + " excluída");
        return "redirect:/rotinas/listar";
    }
}