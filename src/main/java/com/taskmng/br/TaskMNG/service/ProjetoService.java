package com.taskmng.br.TaskMNG.service;

import com.taskmng.br.TaskMNG.dto.ProjetoDTO;
import com.taskmng.br.TaskMNG.entities.Projeto;
import com.taskmng.br.TaskMNG.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    @Transactional
    public ProjetoDTO criarProjeto(Projeto novoProjeto) {
        novoProjeto.setAtivo(1);
        Projeto salvo = projetoRepository.save(novoProjeto);
        return new ProjetoDTO(salvo);
    }

    public List<ProjetoDTO> listarProjetos() {
        return projetoRepository.findAll()
                .stream()
                .filter(p -> p.getAtivo() == 1)
                .map(ProjetoDTO::new)
                .toList();
    }

    @Transactional
    public ProjetoDTO atualizarProjeto(Long id, Projeto projetoAtualizado) {
        Projeto existente = projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "projeto não encontrado"));

        existente.setDescricaoProjeto(projetoAtualizado.getDescricaoProjeto());
        existente.setStatusProjeto(projetoAtualizado.getStatusProjeto());

        Projeto atualizado = projetoRepository.save(existente);
        return new ProjetoDTO(atualizado);
    }

    @Transactional
    public void deletarProjeto(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "projeto não encontrado"));

        if (projeto.getAtivo() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "projeto já está inativo.");

        projeto.setAtivo(0);
        projetoRepository.save(projeto);
    }
}
