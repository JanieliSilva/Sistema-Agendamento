package com.salao.agendamento.service;

import com.salao.agendamento.model.Agendamento;
import com.salao.agendamento.model.Servico;
import com.salao.agendamento.repository.AgendamentoRepository;
import com.salao.agendamento.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime; // ADICIONADO
import java.math.BigDecimal;
import java.util.ArrayList; // ADICIONADO
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    // Injeção necessária para consultar a duração em minutos do serviço selecionado
    @Autowired
    private ServicoRepository servicoRepository;

    public Agendamento salvar(Agendamento agendamento) {
        if (agendamento.getCliente() == null || agendamento.getCliente().getId() == null) {
            throw new RuntimeException("Não é possível agendar: Cliente não informado ou inválido.");
        }

        if (agendamento.getServico() == null || agendamento.getServico().getId() == null) {
            throw new RuntimeException("Não é possível agendar: Serviço não selecionado.");
        }

        // Ajustado para validar o início do agendamento
        if (agendamento.getDataHoraInicio() == null || agendamento.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Erro: A data do agendamento não pode ser no passado!");
        }
        
        // 1. Busca o serviço no banco para descobrir a duração dele
        Servico servico = servicoRepository.findById(agendamento.getServico().getId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));

        // 2. Calculates automaticamente o horário de término do procedimento
        LocalDateTime horaFim = agendamento.getDataHoraInicio().plusMinutes(servico.getDuracaoMinutos());
        agendamento.setDataHoraFim(horaFim);

        // 3. Executa a query que verifica se o bloco completo de tempo está livre
        if (repository.existeChoqueDeHorario(agendamento.getDataHoraInicio(), horaFim)) {
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

    public List<Agendamento> buscarPorData(LocalDate data) {
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(23, 59, 59);
        
        // Ajustado de 'findByDataHoraBetween' para 'findByDataHoraInicioBetween'
        return repository.findByDataHoraInicioBetween(inicioDoDia, fimDoDia);
    }

    public BigDecimal obterFaturamentoTotal() {
        BigDecimal faturamento = repository.calcularFaturamentoTotal();
        return faturamento != null ? faturamento : BigDecimal.ZERO;
    }

    /**
     * NOVO MÉTODO ADICIONADO:
     * Calcula quais horários estão livres no dia com base na duração do serviço.
     */
    public List<LocalTime> buscarHorariosLivres(LocalDate data, Long servicoId) {
        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));

        List<Agendamento> agendamentosDoDia = buscarPorData(data);
        List<LocalTime> horariosLivres = new ArrayList<>();

        // Define o expediente do salão (08:00 às 18:00)
        LocalTime horarioAtual = LocalTime.of(8, 0);
        LocalTime horarioFechamento = LocalTime.of(18, 0);

        // Varre o dia de 30 em 30 minutos procurando brechas livres
        while (horarioAtual.plusMinutes(servico.getDuracaoMinutos()).isBefore(horarioFechamento) || 
               horarioAtual.plusMinutes(servico.getDuracaoMinutos()).equals(horarioFechamento)) {
            
            LocalDateTime inicioSugerido = data.atTime(horarioAtual);
            LocalDateTime fimSugerido = inicioSugerido.plusMinutes(servico.getDuracaoMinutos());

            boolean temConflito = false;
            
            for (Agendamento agendamento : agendamentosDoDia) {
                if (!"CANCELADO".equals(agendamento.getStatus())) {
                    // Checa se o intervalo sugerido conflita com algum agendamento existente
                    if (inicioSugerido.isBefore(agendamento.getDataHoraFim()) && fimSugerido.isAfter(agendamento.getDataHoraInicio())) {
                        temConflito = true;
                        break;
                    }
                }
            }

            if (!temConflito) {
                horariosLivres.add(horarioAtual);
            }

            horarioAtual = horarioAtual.plusMinutes(30);
        }

        return horariosLivres;
    }
}