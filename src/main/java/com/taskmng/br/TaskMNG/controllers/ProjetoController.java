package com.taskmng.br.TaskMNG.controllers;

import com.taskmng.br.TaskMNG.dto.ProjetoDTO;
import com.taskmng.br.TaskMNG.entities.Projeto;
import com.taskmng.br.TaskMNG.entities.Usuario;
import com.taskmng.br.TaskMNG.service.ProjetoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskmng/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ProjetoDTO> cadastrarProjeto(@RequestBody @Valid Projeto novoProjeto, HttpServletRequest request) {
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        ProjetoDTO projetoCriado = projetoService.criarProjeto(novoProjeto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoCriado);
    }

    @GetMapping
    public ResponseEntity<List<ProjetoDTO>> listarProjetos() {
        return ResponseEntity.ok(projetoService.listarProjetos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoDTO> buscarProjetoPorId(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        ProjetoDTO projeto = projetoService.buscarPorId(id, usuarioLogado);
        return ResponseEntity.ok(projeto);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ProjetoDTO> atualizarProjeto(@PathVariable Long id, @RequestBody @Valid Projeto projetoAtualizado, HttpServletRequest request) {
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        ProjetoDTO atualizado = projetoService.atualizarProjeto(id, projetoAtualizado, usuarioLogado);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> deletarProjeto(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        projetoService.deletarProjeto(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}
