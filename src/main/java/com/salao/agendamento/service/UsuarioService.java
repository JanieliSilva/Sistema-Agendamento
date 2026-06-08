package com.salao.agendamento.service;

import com.salao.agendamento.model.Usuario;
import com.salao.agendamento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    /**
     * Cadastra um novo usuário.
     * Valida e-mail duplicado antes de salvar.
     * Role padrão: CLIENTE. Apenas um admin existente pode promover outro usuário.
     */
    public Usuario cadastrar(Usuario usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado.");
        }
        // Garante que cadastro público nunca cria ADMIN
        usuario.setRole("CLIENTE");
        return repository.save(usuario);
    }

    /**
     * Login: valida e-mail e senha.
     * Retorna o usuário sem a senha para o front.
     */
    public UsuarioDTO login(String email, String senha) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail ou senha incorretos."));

        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("E-mail ou senha incorretos.");
        }

        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), usuario.getRole());
    }

    /**
     * Apenas um ADMIN pode promover outro usuário a ADMIN.
     */
    public Usuario promoverParaAdmin(Long usuarioId, Long adminId) {
        Usuario admin = repository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado."));

        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Acesso negado: apenas administradores podem promover usuários.");
        }

        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        usuario.setRole("ADMIN");
        return repository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    // DTO interno: retorna dados do usuário SEM a senha
    public record UsuarioDTO(Long id, String nome, String email, String telefone, String role) {}
}