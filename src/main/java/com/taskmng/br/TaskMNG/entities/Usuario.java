package com.taskmng.br.TaskMNG.entities;

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

    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    private Integer idade;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    private String senha;

    @NotNull
    private Integer ativo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_perfil", nullable = false)
    private Perfil tipoPerfil;

}
