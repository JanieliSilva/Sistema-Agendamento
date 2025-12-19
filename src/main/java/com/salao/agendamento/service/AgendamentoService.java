package com.salao.agendamento.service;

import com.salao.agendamento.model.Agendamento;
import com.salao.agendamento.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    public Agendamento salvar(Agendamento agendamento) {
       
        return repository.save(agendamento);
    }

    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }
    
    public void cancelar(Long id) {
        repository.deleteById(id);
    }
}