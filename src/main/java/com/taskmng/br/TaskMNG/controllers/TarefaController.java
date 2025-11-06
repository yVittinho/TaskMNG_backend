package com.taskmng.br.TaskMNG.controllers;

import com.taskmng.br.TaskMNG.dto.TarefaDTO;
import com.taskmng.br.TaskMNG.entities.Tarefa;
import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.service.TarefaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskmng/tarefa")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Tarefa> cadastrarTarefa(@RequestBody @Valid Tarefa novaTarefa, HttpServletRequest request) {
        Usuario usuarioCriador = (Usuario) request.getSession().getAttribute("usuarioLogado");

        if (usuarioCriador == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Tarefa tarefaCriada = tarefaService.criarTarefa(novaTarefa, usuarioCriador);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaCriada);
    }

    @GetMapping
    public ResponseEntity<List<TarefaDTO>> listarTarefas() {
        return ResponseEntity.ok(tarefaService.listarTarefas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.buscarPorId(id));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody @Valid Tarefa tarefaAtualizada, HttpServletRequest request) {
        Usuario usuarioEditor = (Usuario) request.getSession().getAttribute("usuarioLogado");

        if (usuarioEditor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Tarefa tarefa = tarefaService.atualizarTarefa(id, tarefaAtualizada, usuarioEditor);
        return ResponseEntity.ok(tarefa);
    }


}
