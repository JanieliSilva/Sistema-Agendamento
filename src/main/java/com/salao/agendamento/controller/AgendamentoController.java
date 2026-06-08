package com.salao.agendamento.controller;

import com.salao.agendamento.model.Agendamento;
import com.salao.agendamento.model.Cliente;
import com.salao.agendamento.model.Servico;
import com.salao.agendamento.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    /**
     * GET /agendamentos
     */
    @GetMapping
    public List<Agendamento> listar() {
        return service.listarTodos();
    }

    /**
     * POST /agendamentos
     * Contrato alinhado com o front:
     * Body: {
     *   "clienteId": 1,
     *   "servicoId": 2,
     *   "dataHoraInicio": "2026-07-10T09:00:00"
     * }
     */
    @PostMapping
    public ResponseEntity<Agendamento> cadastrar(@RequestBody Map<String, Object> body) {
        Long clienteId = Long.valueOf(body.get("clienteId").toString());
        Long servicoId = Long.valueOf(body.get("servicoId").toString());
        String dataHoraStr = body.get("dataHoraInicio").toString();

        Agendamento agendamento = new Agendamento();

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        agendamento.setCliente(cliente);

        Servico servico = new Servico();
        servico.setId(servicoId);
        agendamento.setServico(servico);

        agendamento.setDataHoraInicio(java.time.LocalDateTime.parse(dataHoraStr));

        return ResponseEntity.ok(service.salvar(agendamento));
    }

    /**
     * GET /agendamentos/cliente/{id}
     */
    @GetMapping("/cliente/{id}")
    public List<Agendamento> listarPorCliente(@PathVariable Long id) {
        return service.buscarPorCliente(id);
    }

    /**
     * PATCH /agendamentos/{id}/status
     * Body: { "status": "CONCLUIDO" }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Agendamento> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String novoStatus = body.get("status");
        return ResponseEntity.ok(service.atualizarStatus(id, novoStatus));
    }

    /**
     * PATCH /agendamentos/{id}/cancelar
     * Cancela sem deletar — altera status para CANCELADO.
     * Alinhado com o comportamento do front.
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Agendamento> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.atualizarStatus(id, "CANCELADO"));
    }

    /**
     * GET /agendamentos/pordata?data=2026-07-10
     */
    @GetMapping("/pordata")
    public List<Agendamento> buscarPorData(@RequestParam("data") String dataStr) {
        LocalDate data = LocalDate.parse(dataStr);
        return service.buscarPorData(data);
    }

    /**
     * GET /agendamentos/faturamento
     * Retorna JSON: { "valor": 350.00 }
     */
    @GetMapping("/faturamento")
    public ResponseEntity<Map<String, Object>> getFaturamento() {
        return ResponseEntity.ok(Map.of("valor", service.obterFaturamentoTotal()));
    }

    /**
     * GET /agendamentos/horarios-livres?data=2026-07-10&servicoId=1
     */
    @GetMapping("/horarios-livres")
    public ResponseEntity<List<LocalTime>> consultarHorariosLivres(
            @RequestParam LocalDate data,
            @RequestParam Long servicoId) {
        return ResponseEntity.ok(service.buscarHorariosLivres(data, servicoId));
    }
}