package com.taskmng.br.TaskMNG.service;

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


}
