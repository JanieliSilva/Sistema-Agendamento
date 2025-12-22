package com.salao.agendamento.repository;

import com.salao.agendamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional; 
import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    List<Agendamento> findByClienteId(Long clienteId);

    boolean existsByDataHora(LocalDateTime dataHora);
}