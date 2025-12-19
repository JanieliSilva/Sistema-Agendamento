package com.salao.agendamento.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity // Diz ao banco que isso será uma tabela
@Data   // O Lombok cria os Getters e Setters sozinho
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeDona;

    @Column(nullable = false)
    private String nomeSalao;

    private String whatsapp;
}