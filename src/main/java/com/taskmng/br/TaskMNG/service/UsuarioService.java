package com.taskmng.br.TaskMNG.service;

import com.taskmng.br.TaskMNG.dto.UsuarioDTO;
import com.taskmng.br.TaskMNG.dto.UsuarioUpdateDTO;
import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.enums.Perfil;
import com.taskmng.br.TaskMNG.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
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
        String senhaHash = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaHash);

        novoUsuario.setAtivo(1);

        return usuarioRepository.save(novoUsuario);
    }

    // trazendo todos os usuarios(ativos)
    public List<UsuarioDTO> exibirUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getAtivo() == 1)
                .map(u -> new UsuarioDTO(
                        u.getNome(),
                        u.getIdade(),
                        u.getTipoPerfil()
                ))
                .toList();
    }

    // trazendo o usuario pelo id
    public Usuario buscarPorId (Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "usuário não encontrado"));
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "usuário não encontrado"));

        if (dto.nome() != null && !dto.nome().isBlank()) {
            usuarioExistente.setNome(dto.nome());
        }

        if (dto.idade() != null) {
            usuarioExistente.setIdade(dto.idade());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            usuarioExistente.setEmail(dto.email());
        }
        return usuarioRepository.save(usuarioExistente);
    }

    private boolean senhaValida(String senha) {
        // regex 8–14 chars, pelo menos 1 maiúscula, 1 minúscula, 1 número e 1 caractere especial
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$";
        return senha.matches(regex);
    }

    //exclusao lógica do usuário
    @Transactional
    public void deletarUsuario(Long id, Usuario usuarioLogado) {
        if (usuarioLogado.getTipoPerfil() != Perfil.ADMINISTRADOR) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "apenas administradores podem excluir usuários.");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "usuário não encontrado para exclusão"));

        if (usuario.getAtivo() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "usuário já está inativo.");
        }

        usuario.setAtivo(0);
        usuarioRepository.save(usuario);
    }

    public boolean autenticar(String email, String senhaInformada){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "usuário não encontrado"));

        boolean senhaCorreta = passwordEncoder.matches(senhaInformada, usuario.getSenha());

        if(!senhaCorreta){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "senha incorreta.");
        }
        return true;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuário não encontrado"));
    }
}
