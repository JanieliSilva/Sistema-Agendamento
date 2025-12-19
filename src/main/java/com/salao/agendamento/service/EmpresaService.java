package com.salao.agendamento.service;

import com.salao.agendamento.model.Empresa;
import com.salao.agendamento.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    public Empresa salvar(Empresa empresa) {
        return repository.save(empresa);
    }
}
