package com.salao.agendamento;

import com.salao.agendamento.model.Cliente;
import com.salao.agendamento.model.Empresa;
import com.salao.agendamento.service.ClienteService;
import com.salao.agendamento.service.EmpresaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgendamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendamentoApplication.class, args);
	}

	@Bean
	CommandLineRunner teste(EmpresaService service, ClienteService clienteService) {
		return args -> {
			Empresa e = new Empresa();
			e.setNomeSalao("Salão da Danielle");
			e.setNomeDona("Danielle Fernandes");
			e.setWhatsapp("11999999999");

			service.salvar(e);
			System.out.println("EMPRESA SALVA COM SUCESSO NO BANCO!");

			Cliente novoCliente = new Cliente();
			novoCliente.setNome("Janieli Desenvolvedora");
			novoCliente.setTelefone("11999998888");
			clienteService.salvar(novoCliente);
			System.out.println(" Cliente salvo com sucesso!");
		};
	}
}