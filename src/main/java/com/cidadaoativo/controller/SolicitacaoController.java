package com.cidadaoativo.controller;
import com.cidadaoativo.dto.SolicitacaoDTO;
import com.cidadaoativo.dto.AtualizarStatusDTO;
import com.cidadaoativo.entity.Solicitacao;
import com.cidadaoativo.service.SolicitacaoService;
import com.cidadaoativo.service.UsuarioService;
import com.cidadaoativo.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SolicitacaoController {
    private final SolicitacaoService solicitacaoService;
    private final UsuarioService usuarioService;
    @PostMapping
    public ResponseEntity<?> criarSolicitacao(@RequestBody SolicitacaoDTO dto) {
        try {
            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setCategoria(dto.getCategoria());
            solicitacao.setDescricao(dto.getDescricao());
            solicitacao.setLocalizacao(dto.getLocalizacao());
            solicitacao.setBairro(dto.getBairro());
            solicitacao.setPrioridade(dto.getPrioridade());
            solicitacao.setAnonimo(dto.getAnonimo());
            if (!dto.getAnonimo() && dto.getCidadaoId() != null) {
                solicitacao.setCidadao(usuarioService.buscarPorId(dto.getCidadaoId()).orElse(null));
            }
            Solicitacao criada = solicitacaoService.criar(solicitacao);
            return ResponseEntity.ok(toDTO(criada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<?> buscarPorProtocolo(@PathVariable String protocolo) {
        return solicitacaoService.buscarPorProtocolo(protocolo)
                .map(s -> ResponseEntity.ok(toDTO(s)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return solicitacaoService.buscarPorId(id)
                .map(s -> ResponseEntity.ok(toDTO(s)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/bairro/{bairro}")
    public ResponseEntity<?> listarPorBairro(@PathVariable String bairro) {
        List<Solicitacao> solicitacoes = solicitacaoService.listarPorBairro(bairro);
        return ResponseEntity.ok(solicitacoes.stream().map(this::toDTO).toList());
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<?> listarPorStatus(@PathVariable String status) {
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            List<Solicitacao> solicitacoes = solicitacaoService.listarPorStatus(statusEnum);
            return ResponseEntity.ok(solicitacoes.stream().map(this::toDTO).toList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Status inválido"));
        }
    }
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        List<Solicitacao> solicitacoes = solicitacaoService.listarTodas();
        return ResponseEntity.ok(solicitacoes.stream().map(this::toDTO).toList());
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusDTO dto) {
        try {
            var responsavel = usuarioService.buscarPorId(dto.getResponsavelId()).orElse(null);
            Status novoStatus = Status.valueOf(dto.getStatus().toUpperCase());
            Solicitacao atualizada = solicitacaoService.atualizarStatus(id, novoStatus, responsavel, dto.getMotivo());
            return ResponseEntity.ok(toDTO(atualizada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
    private SolicitacaoDTO toDTO(Solicitacao solicitacao) {
        SolicitacaoDTO dto = new SolicitacaoDTO();
        dto.setId(solicitacao.getId());
        dto.setProtocolo(solicitacao.getProtocolo());
        dto.setCategoria(solicitacao.getCategoria());
        dto.setDescricao(solicitacao.getDescricao());
        dto.setLocalizacao(solicitacao.getLocalizacao());
        dto.setBairro(solicitacao.getBairro());
        dto.setStatus(solicitacao.getStatus());
        dto.setPrioridade(solicitacao.getPrioridade());
        dto.setDataCriacao(solicitacao.getDataCriacao());
        dto.setPrazoSLA(solicitacao.getPrazoSLA());
        return dto;
    }
}