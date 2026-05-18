package com.salao.agendamento.repository;

import com.salao.agendamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Busca todos os agendamentos de um cliente específico
    List<Agendamento> findByClienteId(Long clienteId);

  @Query("SELECT a FROM Agendamento a WHERE a.dataHoraInicio BETWEEN :inicio AND :fim")
List<Agendamento> findByDataHoraInicioBetween(@Param("inicio") java.time.LocalDateTime inicio, @Param("fim") java.time.LocalDateTime fim);

    // Calcula o faturamento apenas dos serviços já concluídos
    @Query("SELECT SUM(a.servico.preco) FROM Agendamento a WHERE a.status = 'CONCLUIDO'")
    BigDecimal calcularFaturamentoTotal();

    // A nossa query mágica para evitar que um cliente marque no horário do outro
    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.status != 'CANCELADO' AND (:inicio < a.dataHoraFim AND :fim > a.dataHoraInicio)")
    boolean existeChoqueDeHorario(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}