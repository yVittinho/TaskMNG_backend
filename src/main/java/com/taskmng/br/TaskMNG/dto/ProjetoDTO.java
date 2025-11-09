package com.taskmng.br.TaskMNG.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.taskmng.br.TaskMNG.entities.Projeto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProjetoDTO(
        String descricaoProjeto,
        String statusProjeto
) {
    public ProjetoDTO(Projeto projeto) {
        this(
                projeto.getDescricaoProjeto(),
                projeto.getStatusProjeto() != null ? projeto.getStatusProjeto().name() : null
        );
    }

}
