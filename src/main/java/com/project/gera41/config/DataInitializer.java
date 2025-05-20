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
        rotina1.setNome("Cadastro de clientes");
        rotina1.setDescricao("Cliente é a entidade que possui necessidades de produtos e serviços a serem supridas pelas empresas.\n" +
                "Conceitualmente, na maioria das vezes em que é emitido um documento de saída, o destinatário é considerado um cliente, independentemente do tipo que ele possua ou da denominação que a empresa tenha determinado para ele.");
        rotina1.setCategoria("Cadastro");
        rotina1.setAtivo(true);
        rotina1.setPrincipaisObjetivos("Principais objetivos");
        rotina1.setOrigensDados("Manualmente ou via importação TOTVS Protheus Mile (Para carga inicial).\n");
        rotina1.setFatoresCriticosSucesso("Todos os envolvidos no processo de faturamento devem estar capacitados.\n" +
                "Padronização dos processos para todas as empresas contempladas no escopo do projeto\n");
        rotina1.setRestricoes("Este cadastro será utilizado com a funcionalidade padrão do sistema Protheus.");
        rotinaRepository.save(rotina1);
    }
}