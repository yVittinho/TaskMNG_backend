package com.taskmng.br.TaskMNG.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskmng.br.TaskMNG.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author : Murillo Monteiro Aragão
 *
 */

@Entity
@Table(name = "Usuario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotBlank
    private String nome;

    @NotNull
    private Integer idade;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @JsonIgnore
    private String senha;

    @NotNull
    private Integer ativo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_perfil", nullable = false)
    private Perfil tipoPerfil;
}
