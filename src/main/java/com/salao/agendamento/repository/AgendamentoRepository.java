package com.salao.agendamento.repository;

import com.salao.agendamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;
@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByClienteId(Long clienteId);
    boolean existsByDataHora(LocalDateTime dataHora);
    
    
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    @Query("SELECT SUM(a.servico.preco) FROM Agendamento a WHERE a.status = 'CONCLUIDO'")
    java.math.BigDecimal calcularFaturamentoTotal();
}