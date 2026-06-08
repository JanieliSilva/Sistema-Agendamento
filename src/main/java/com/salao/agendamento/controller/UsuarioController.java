package com.salao.agendamento.controller;

import com.salao.agendamento.model.Usuario;
import com.salao.agendamento.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    /**
     * POST /usuarios
     * Cadastro público — sempre cria como CLIENTE.
     * Body: { "nome", "email", "senha", "telefone" }
     */
    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.cadastrar(usuario));
    }

    /**
     * POST /usuarios/login
     * Body: { "email": "...", "senha": "..." }
     * Retorna o usuário sem a senha + role para o front controlar o menu.
     */
    @PostMapping("/login")
    public ResponseEntity<UsuarioService.UsuarioDTO> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");
        return ResponseEntity.ok(service.login(email, senha));
    }

    /**
     * PATCH /usuarios/{id}/promover?adminId={adminId}
     * Promove um usuário a ADMIN. Só funciona se quem chama já for ADMIN.
     */
    @PatchMapping("/{id}/promover")
    public ResponseEntity<Usuario> promover(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        return ResponseEntity.ok(service.promoverParaAdmin(id, adminId));
    }

    /**
     * GET /usuarios
     * Lista todos os usuários (uso administrativo).
     */
    @GetMapping
    public List<Usuario> listar() {
        return service.listarTodos();
    }
}