package com.salao.agendamento.controller;

import com.salao.agendamento.model.Servico;
import com.salao.agendamento.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService service;

    /** GET /servicos */
    @GetMapping
    public List<Servico> listar() {
        return service.listarTodos();
    }

    /** POST /servicos */
    @PostMapping
    public ResponseEntity<Servico> cadastrar(@RequestBody Servico servico) {
        return ResponseEntity.ok(service.salvar(servico));
    }

    /** DELETE /servicos/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}