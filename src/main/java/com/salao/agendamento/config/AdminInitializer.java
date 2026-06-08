package com.salao.agendamento.config;

import com.salao.agendamento.model.Usuario;
import com.salao.agendamento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Cria o primeiro administrador automaticamente ao subir o servidor,
 * se ele ainda não existir no banco.
 *
 * Em produção: troque a senha aqui ou mova para variável de ambiente.
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void run(String... args) {
        String emailAdmin = "admin@empresa.com";

        if (!repository.existsByEmail(emailAdmin)) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail(emailAdmin);
            admin.setSenha("admin123"); // Trocar em produção!
            admin.setRole("ADMIN");
            repository.save(admin);
            System.out.println(">>> Admin padrão criado: " + emailAdmin);
        }
    }
}