package com.taskmng.br.TaskMNG.service;

import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //public Usuario criarUsuario(Usuario usuario) {

        //if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email já cadastrado.");
        //}

    //}
}
