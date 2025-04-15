package com.project.gera41.config;

import com.project.gera41.models.Rotina;
import com.project.gera41.repositories.RotinaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RotinaRepository rotinaRepository) {
        return args -> {
            Rotina rotina1 = new Rotina();
            rotina1.setCodigo("PC001");
            rotina1.setNome("Pedido de Compra");
            rotina1.setDescricao("Permite registrar solicitações de compra no sistema ERP.");
            rotina1.setCategoria("ERP");
            rotina1.setAtivo(true);

            Rotina rotina2 = new Rotina();
            rotina2.setCodigo("FT001");
            rotina2.setNome("Faturamento");
            rotina2.setDescricao("Geração de notas fiscais e controle de vendas.");
            rotina2.setCategoria("ERP");
            rotina2.setAtivo(true);

            Rotina rotina3 = new Rotina();
            rotina3.setCodigo("ES001");
            rotina3.setNome("Estoque");
            rotina3.setDescricao("Controle de entrada e saída de produtos.");
            rotina3.setCategoria("ERP");
            rotina3.setAtivo(true);

            rotinaRepository.saveAll(List.of(rotina1, rotina2, rotina3));
        };
    }
}