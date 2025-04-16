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
        rotina1.setCodigo("CDFT001");
        rotina1.setNome("Clientes");
        rotina1.setDescricao("Cliente é a entidade que possui necessidades de produtos e serviços a serem supridas pelas empresas.\n" +
                "Conceitualmente, na maioria das vezes em que é emitido um documento de saída, o destinatário é considerado um cliente, independentemente do tipo que ele possua ou da denominação que a empresa tenha determinado para ele.\n" +
                "\n" +
                "No ambiente FATURAMENTO, o cadastro de Clientes é uma etapa obrigatória para que os pedidos de vendas sejam registrados e os documentos de saída possam ser gerados. É possível classificar os clientes em cinco tipos:\n");
        rotina1.setCategoria("Cadastro");
        rotina1.setAtivo(true);
        rotinaRepository.save(rotina1);

        Rotina rotina2 = new Rotina();
        rotina2.setCodigo("ROT002");
        rotina2.setNome("Condição de pagamento");
        rotina2.setDescricao("As negociações de compras e vendas de produtos ou serviços, normalmente, se baseiam nas condições de pagamento. Elas determinam como e quando serão efetuados os pagamentos, especificando datas de vencimentos, número e valores das parcelas, descontos e acréscimos.\n" +
                "\n" +
                "Conforme o tipo da condição, o sistema irá tratar de forma diferenciada o conteúdo dos campos Código e Cond. Pagto, o que permite a configuração de diferentes condições de pagamento, para aplicação aos pagamentos tanto de fornecedores como de clientes\n");
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