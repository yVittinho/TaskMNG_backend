package com.taskmng.br.TaskMNG.service;

import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.enums.Perfil;
import com.taskmng.br.TaskMNG.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario novoUsuario, Perfil perfilCriador) {

        if(perfilCriador != Perfil.ADMINISTRADOR && perfilCriador != Perfil.TECHLEAD){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "apenas administradores ou techleads podem cadastrar usuários");

        }

        if (usuarioRepository.existsByEmail(novoUsuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email já cadastrado.");

        }

        if(novoUsuario.getTipoPerfil() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "perfil do usuário é obrigatório");
        }

        //lembrar de validar a senha com regex

        //passando criptografia na senha para armazenar no banco
        String senhaHash = new BCryptPasswordEncoder().encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaHash);

        return usuarioRepository.save(novoUsuario);
    }

    // trazendo todos os usuarios
    public List<Usuario> exibirUsuarios(){
        return usuarioRepository.findAll();
    }

    // trazendo o usuario pelo id
    public Usuario buscarPorId (Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "usuário não encontrado"));
    }

    //


}
