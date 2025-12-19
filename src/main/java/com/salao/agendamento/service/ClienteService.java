package com.salao.agendamento.service;

import com.salao.agendamento.model.Cliente;
import com.salao.agendamento.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    // Salvar ou atualizar um cliente
    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    // Listar todos os clientes cadastrados
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    // Buscar um cliente específico pelo ID
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Deletar um cliente
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}