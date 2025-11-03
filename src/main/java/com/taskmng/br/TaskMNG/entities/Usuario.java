package com.taskmng.br.TaskMNG.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotBlank
    @NotNull
    private Integer idade;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    //passar o hash de senha aqui dps
    private String senha;

    @NotNull
    private Integer ativo;

    @ManyToOne() //lembrar de colocar dps
    @JoinColumn(name = "fk_tipo_perfil", nullable = false)
    private Perfil idPerfilUsuario;

    // criar os getters e setters para puxar o perfil do usuario.
    //
}
