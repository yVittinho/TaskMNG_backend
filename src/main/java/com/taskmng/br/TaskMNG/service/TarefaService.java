package com.taskmng.br.TaskMNG.service;

import com.taskmng.br.TaskMNG.dto.TarefaDTO;
import com.taskmng.br.TaskMNG.dto.TarefaUpdateDTO;
import com.taskmng.br.TaskMNG.entities.Projeto;
import com.taskmng.br.TaskMNG.entities.Tarefa;
import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.enums.Perfil;
import com.taskmng.br.TaskMNG.enums.Status;
import com.taskmng.br.TaskMNG.repository.ProjetoRepository;
import com.taskmng.br.TaskMNG.repository.TarefaRepository;
import com.taskmng.br.TaskMNG.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProjetoRepository projetoRepository;

    @Transactional
    public TarefaDTO criarTarefa(Tarefa novaTarefa, Usuario techLead) {
        if (techLead == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário não autenticado.");

        if (techLead.getTipoPerfil() != Perfil.TECHLEAD)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "apenas TECHLEAD pode criar tarefas.");

        if (novaTarefa.getColaborador() == null || novaTarefa.getColaborador().getIdUsuario() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "colaborador deve ser informado.");

        Usuario colaborador = usuarioRepository.findById(novaTarefa.getColaborador().getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "colaborador não encontrado."));

        Projeto projeto = projetoRepository.findById(novaTarefa.getProjeto().getIdProjeto())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "projeto não encontrado."));

        novaTarefa.setAtivo(1);
        novaTarefa.setDataCriacao(new Date());
        novaTarefa.setStatus(Status.PENDENTE);
        novaTarefa.setTechLead(techLead);
        novaTarefa.setColaborador(colaborador);
        novaTarefa.setProjeto(projeto);

        Tarefa tarefaSalva = tarefaRepository.save(novaTarefa);
        return new TarefaDTO(tarefaSalva);
    }

    public List<TarefaDTO> listarTarefas() {
        return tarefaRepository.findAll()
                .stream()
                .filter(t -> t.getAtivo() == 1)
                .map(TarefaDTO::new)
                .toList();
    }

    public TarefaDTO buscarPorId(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tarefa não encontrada."));

        if (tarefa.getAtivo() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tarefa inativa.");

        return new TarefaDTO(tarefa);
    }

    @Transactional
    public TarefaDTO atualizarTarefa(Long id, TarefaUpdateDTO dto, Usuario usuarioEditor) {
        Tarefa existente = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tarefa não encontrada."));

        if (usuarioEditor.getTipoPerfil() != Perfil.TECHLEAD)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "apenas TECHLEAD pode atualizar tarefas.");

        existente.setNomeTarefa(dto.nomeTarefa());
        existente.setDescricao(dto.descricao());
        existente.setDataEntrega(dto.dataEntrega());
        existente.setPrioridade(dto.prioridade());
        existente.setStatus(dto.status());

        Tarefa atualizada = tarefaRepository.save(existente);
        return new TarefaDTO(atualizada);
    }

    public TarefaDTO entregarTarefa(Long id, Usuario colaborador) {
        if (colaborador == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário não autenticado.");

        if (colaborador.getTipoPerfil() != Perfil.COLABORADOR)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "apenas colaboradores podem entregar tarefas.");

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tarefa não encontrada."));

        tarefa.setStatus(Status.ENTREGUE);
        tarefa.setDataEntrega(new Date());

        Tarefa entregue = tarefaRepository.save(tarefa);
        return new TarefaDTO(entregue);
    }

    @Transactional
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
