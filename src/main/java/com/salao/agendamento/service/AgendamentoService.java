package com.salao.agendamento.service;

import com.salao.agendamento.model.Agendamento;
import com.salao.agendamento.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    public Agendamento salvar(Agendamento agendamento) {
    if (agendamento.getCliente() == null || agendamento.getCliente().getId() == null) {
        throw new RuntimeException("Não é possível agendar: Cliente não informado ou inválido.");
    }

    if (agendamento.getServico() == null || agendamento.getServico().getId() == null) {
        throw new RuntimeException("Não é possível agendar: Serviço não selecionado.");
    }

    if (agendamento.getDataHora() == null || agendamento.getDataHora().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Erro: A data do agendamento não pode ser no passado!");
    }
    
    if (repository.existsByDataHora(agendamento.getDataHora())) {
        throw new RuntimeException("Erro: Este horário já está ocupado por outro cliente!");
    }
   
        return repository.save(agendamento);
    }

    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }
    
    public void cancelar(Long id) {
        repository.deleteById(id);
    }

    public List<Agendamento> buscarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public Agendamento atualizarStatus(Long id, String novoStatus) {
    Agendamento agendamento = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + id));

    agendamento.setStatus(novoStatus.toUpperCase());
    return repository.save(agendamento);
     }

   }
