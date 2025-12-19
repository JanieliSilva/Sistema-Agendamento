package com.salao.agendamento.controller;

import com.salao.agendamento.model.Empresa;
import com.salao.agendamento.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService service;

    // ADICIONE ESTE BLOCO ABAIXO:
    @GetMapping
    public List<Empresa> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public Empresa cadastrar(@RequestBody Empresa empresa) {
        return service.salvar(empresa);
    }
}