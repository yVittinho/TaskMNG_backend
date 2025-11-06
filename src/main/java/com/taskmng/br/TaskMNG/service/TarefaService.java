package com.taskmng.br.TaskMNG.service;

import com.taskmng.br.TaskMNG.dto.TarefaDTO;
import com.taskmng.br.TaskMNG.entities.Tarefa;
import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.enums.Perfil;
import com.taskmng.br.TaskMNG.enums.Status;
import com.taskmng.br.TaskMNG.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public Tarefa criarTarefa(Tarefa novaTarefa, Usuario usuarioCriador) {
        if (usuarioCriador == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "usuário não autenticado.");
        }

        if (usuarioCriador.getTipoPerfil() != Perfil.ADMINISTRADOR && usuarioCriador.getTipoPerfil() != Perfil.TECHLEAD) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "apenas administradores e techleads podem criar tarefas.");
        }
        novaTarefa.setAtivo(1);
        novaTarefa.setDataCriacao(new Date());
        novaTarefa.setStatus(Status.PENDENTE);

        return tarefaRepository.save(novaTarefa);
    }

    public List<TarefaDTO> listarTarefas() {
        return tarefaRepository.findAll()
                .stream()
                .filter(t -> t.getAtivo() == 1)
                .map(t -> new TarefaDTO(
                        t.getNomeTarefa(),
                        t.getDescricao(),
                        t.getDataCriacao(),
                        t.getDataEntrega(),
                        t.getPrioridade(),
                        t.getStatus()
                ))
                .toList();
    }

    public TarefaDTO buscarPorId(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tarefa não encontrada."));

        return new TarefaDTO(
                tarefa.getNomeTarefa(),
                tarefa.getDescricao(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getPrioridade(),
                tarefa.getStatus()
        );
    }

    public Tarefa atualizarTarefa(Long id, Tarefa tarefaAtualizada, Usuario usuarioEditor) {
        if (usuarioEditor == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário não autenticado.");

        if (usuarioEditor.getTipoPerfil() != Perfil.TECHLEAD)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "apenas TECHLEAD pode atualizar tarefas.");

        Tarefa existente = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tarefa não encontrada."));

        existente.setNomeTarefa(tarefaAtualizada.getNomeTarefa());
        existente.setDescricao(tarefaAtualizada.getDescricao());
        existente.setDataEntrega(tarefaAtualizada.getDataEntrega());
        existente.setPrioridade(tarefaAtualizada.getPrioridade());
        existente.setStatus(tarefaAtualizada.getStatus());

        return tarefaRepository.save(existente);
    }

    public Tarefa entregarTarefa(Long id, Usuario colaborador) {
        if (colaborador == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário não autenticado.");

        if (colaborador.getTipoPerfil() != Perfil.COLABORADOR)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "apenas colaboradores podem entregar tarefas.");

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tarefa não encontrada."));

        tarefa.setStatus(Status.ENTREGUE);
        tarefa.setDataEntrega(new Date());

        return tarefaRepository.save(tarefa);
    }

    public void deletarTarefa(Long id, Usuario usuarioExclusao) {
        if (usuarioExclusao == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário não autenticado.");

        if (usuarioExclusao.getTipoPerfil() != Perfil.TECHLEAD)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "apenas TECHLEAD pode excluir tarefas.");

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tarefa não encontrada."));

        if (tarefa.getAtivo() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tarefa já está inativa.");

        tarefa.setAtivo(0);
        tarefaRepository.save(tarefa);
    }


}
