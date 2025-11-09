package com.taskmng.br.TaskMNG.service;

import com.taskmng.br.TaskMNG.dto.ProjetoDTO;
import com.taskmng.br.TaskMNG.entities.Projeto;
import com.taskmng.br.TaskMNG.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
