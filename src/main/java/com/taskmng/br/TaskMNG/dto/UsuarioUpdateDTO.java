package com.taskmng.br.TaskMNG.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioUpdateDTO(
        @NotBlank
        String nome,

        @NotNull
        Integer idade,

        @Email
        @NotBlank
        String email
) {}
