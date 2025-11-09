package com.taskmng.br.TaskMNG.controllers;


import com.taskmng.br.TaskMNG.dto.LoginDTO;
import com.taskmng.br.TaskMNG.dto.UsuarioDTO;
import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/taskmng/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        boolean autenticado = usuarioService.autenticar(loginDTO.email(), loginDTO.senha());

        if (autenticado) {
            Usuario usuario = usuarioService.buscarPorEmail(loginDTO.email());
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogado", usuario);

            return ResponseEntity.ok("login realizado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("credenciais inválidas.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    //verificando o usuario logado no front
    @GetMapping("/verify")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UsuarioDTO dto = new UsuarioDTO(usuario.getNome(), usuario.getIdade(), usuario.getTipoPerfil());
        return ResponseEntity.ok(dto);
    }
}

