package com.salao.agendamento.controller;

import com.salao.agendamento.model.Cliente;
import com.salao.agendamento.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes") 
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping 
    public List<Cliente> listar() {
        return service.listarTodos();
    }

    @PostMapping 
    public Cliente cadastrar(@RequestBody Cliente cliente) {
        return service.salvar(cliente);
    }
}