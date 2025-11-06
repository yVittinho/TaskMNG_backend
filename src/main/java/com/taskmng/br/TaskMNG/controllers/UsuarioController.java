package com.taskmng.br.TaskMNG.controllers;


import com.taskmng.br.TaskMNG.dto.UsuarioDTO;
import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.enums.Perfil;
import com.taskmng.br.TaskMNG.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskmng/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody @Valid Usuario novoUsuario, @RequestParam Perfil perfilCriador) {
        Usuario usuarioCriado = usuarioService.cadastrarUsuario(novoUsuario, perfilCriador);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios (){
        return ResponseEntity.ok(usuarioService.exibirUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity <Usuario> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado, HttpServletRequest request) {
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuarioAtualizado));
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioExclusao, HttpServletRequest request){
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        usuarioService.deletarUsuario(id, usuarioExclusao);
        return ResponseEntity.noContent().build();
    }




}
