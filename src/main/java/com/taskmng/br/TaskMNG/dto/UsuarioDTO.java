package com.taskmng.br.TaskMNG.dto;

import com.taskmng.br.TaskMNG.enums.Perfil;

public record UsuarioDTO(
        String nome,
        Integer idade,
        String email,
        Perfil tipoPerfil
) {}
