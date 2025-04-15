package com.project.gera41.config;

import com.project.gera41.models.Rotina;
import com.project.gera41.repositories.RotinaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RotinaRepository rotinaRepository;

    public DataInitializer(RotinaRepository rotinaRepository) {
        this.rotinaRepository = rotinaRepository;
    }

    @Override
    public void run(String... args) {
        Rotina rotina1 = new Rotina();
        rotina1.setCodigo("ROT001");
        rotina1.setNome("Rotina de Cadastro");
        rotina1.setDescricao("Gerenciamento de cadastros de clientes");
        rotina1.setCategoria("Cadastro");
        rotina1.setAtivo(true);
        rotinaRepository.save(rotina1);

        Rotina rotina2 = new Rotina();
        rotina2.setCodigo("ROT002");
        rotina2.setNome("Rotina de Relatórios");
        rotina2.setDescricao("Geração de relatórios financeiros");
        rotina2.setCategoria("Relatórios");
        rotina2.setAtivo(true);
        rotinaRepository.save(rotina2);

        Rotina rotina3 = new Rotina();
        rotina3.setCodigo("ROT003");
        rotina3.setNome("Rotina de Exportação");
        rotina3.setDescricao("Exportação de dados para sistemas externos");
        rotina3.setCategoria("Integração");
        rotina3.setAtivo(true);
        rotinaRepository.save(rotina3);
    }
}