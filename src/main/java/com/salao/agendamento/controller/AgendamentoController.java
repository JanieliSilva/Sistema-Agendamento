package com.salao.agendamento.controller;

import com.salao.agendamento.model.Agendamento;
import com.salao.agendamento.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
    @Autowired
    private AgendamentoService service;

    @GetMapping
    public List<Agendamento> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public Agendamento criar(@RequestBody Agendamento agendamento) {
        return service.salvar(agendamento);
    }
} 