package com.taskmng.br.TaskMNG.entities;

import com.taskmng.br.TaskMNG.enums.Prioridade;
import com.taskmng.br.TaskMNG.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 *
 * @author : Murillo Monteiro Aragão
 *
 */

@Entity
@Table(name = "Tarefa")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa")
    private Long idTarefa;

    @NotBlank
    @Column(name = "nome_tarefa")
    private String nomeTarefa;

    @NotBlank
    private String descricao;

    @NotNull
    private Integer ativo;

    @Column(name = "data_criacao")
    private Date dataCriacao;

    @Column(name = "data_entrega")
    private Date dataEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "techlead_id", nullable = false)
    private Usuario techLead;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Usuario colaborador;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;
}
