package com.taskmng.br.TaskMNG.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarSenhaDTO(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String novaSenha,
        @NotBlank
        String confirmarSenha
) { }
