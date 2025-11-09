package com.taskmng.br.TaskMNG.entities;

import com.taskmng.br.TaskMNG.enums.StatusProjeto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Projeto")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projeto")
    private Long idProjeto;

    @NotBlank
    @Column(name = "descricao_projeto")
    private String descricaoProjeto;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_projeto")
    private StatusProjeto statusProjeto;

    @NotNull
    private Integer ativo;
}
