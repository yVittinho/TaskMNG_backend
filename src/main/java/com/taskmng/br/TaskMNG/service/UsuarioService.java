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
import java.util.Optional;


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
        if (!senhaValida(novoUsuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "senha inválida. A senha deve conter entre 8 e 14 caracteres, incluindo maiúscula, minúscula, número e caractere especial.");
        }

        //passando criptografia na senha para armazenar no banco
        String senhaHash = new BCryptPasswordEncoder().encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaHash);

        novoUsuario.setAtivo(1);

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

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado){
        //usando optional para verificar se o usuario existe
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        Usuario usuarioExistente = optUsuario.get();

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setIdade(usuarioAtualizado.getIdade());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());

        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            if (!senhaValida(usuarioAtualizado.getSenha())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "senha inválida.");
            }
            String senhaHash = new BCryptPasswordEncoder().encode(usuarioAtualizado.getSenha());
            usuarioExistente.setSenha(senhaHash);
        }
        return usuarioRepository.save(usuarioExistente);
    }

    private boolean senhaValida(String senha) {
        // regex 8–14 chars, pelo menos 1 maiúscula, 1 minúscula, 1 número e 1 caractere especial
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$";
        return senha.matches(regex);
    }
}
