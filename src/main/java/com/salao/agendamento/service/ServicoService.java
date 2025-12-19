package com.salao.agendamento.service;

import com.salao.agendamento.model.Servico;
import com.salao.agendamento.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository repository;

    public Servico salvar(Servico servico) {
        return repository.save(servico);
    }

    public List<Servico> listarTodos() {
        return repository.findAll();
    }
}