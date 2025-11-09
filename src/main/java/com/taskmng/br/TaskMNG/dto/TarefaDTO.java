package com.taskmng.br.TaskMNG.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.taskmng.br.TaskMNG.entities.Tarefa;
import com.taskmng.br.TaskMNG.enums.Prioridade;
import com.taskmng.br.TaskMNG.enums.Status;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TarefaDTO(
        String nomeTarefa,
        String descricao,
        Date dataCriacao,
        Date dataEntrega,
        String prioridade,
        String status,
        String colaboradorDesignado,
        String techLeadResponsavel,
        String descricaoProjeto
) {
    public TarefaDTO(Tarefa tarefa) {
        this(
                tarefa.getNomeTarefa(),
                tarefa.getDescricao(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getPrioridade() != null ? tarefa.getPrioridade().name() : null,
                tarefa.getStatus() != null ? tarefa.getStatus().name() : null,
                tarefa.getColaborador() != null ? tarefa.getColaborador().getNome() : null,
                tarefa.getTechLead() != null ? tarefa.getTechLead().getNome() : null,
                tarefa.getProjeto() != null ? tarefa.getProjeto().getDescricaoProjeto() : null
        );
    }
}
