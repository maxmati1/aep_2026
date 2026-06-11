package com.cidadaoativo.service;
import com.cidadaoativo.entity.Solicitacao;
import com.cidadaoativo.entity.HistoricoStatus;
import com.cidadaoativo.entity.Usuario;
import com.cidadaoativo.enums.Prioridade;
import com.cidadaoativo.enums.Status;
import com.cidadaoativo.repository.SolicitacaoRepository;
import com.cidadaoativo.repository.HistoricoStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional
public class SolicitacaoService {
    private final SolicitacaoRepository solicitacaoRepository;
    private final HistoricoStatusRepository historicoStatusRepository;
    public Solicitacao criar(Solicitacao solicitacao) {
        validarSolicitacao(solicitacao);
        calcularPrazoSLA(solicitacao);
        Solicitacao criada = solicitacaoRepository.save(solicitacao);
        registrarHistoricoInicial(criada);
        return criada;
    }
    public Optional<Solicitacao> buscarPorProtocolo(String protocolo) {
        return solicitacaoRepository.findByProtocolo(protocolo);
    }
    public Optional<Solicitacao> buscarPorId(Long id) {
        return solicitacaoRepository.findById(id);
    }
    public List<Solicitacao> listarPorStatus(Status status) {
        return solicitacaoRepository.findByStatus(status);
    }
    public List<Solicitacao> listarPorBairro(String bairro) {
        return solicitacaoRepository.findByBairro(bairro);
    }
    public List<Solicitacao> listarTodas() {
        return solicitacaoRepository.findAll();
    }
    public Solicitacao atualizarStatus(Long solicitacaoId, Status novoStatus, Usuario responsavel, String motivo) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
        Status statusAnterior = solicitacao.getStatus();
        solicitacao.setStatus(novoStatus);
        if (novoStatus == Status.RESOLVIDO) {
            solicitacao.setDataResolucao(LocalDateTime.now());
        }
        Solicitacao atualizada = solicitacaoRepository.save(solicitacao);
        registrarHistoricoTransicao(atualizada, statusAnterior, novoStatus, responsavel, motivo);
        return atualizada;
    }
    private void calcularPrazoSLA(Solicitacao solicitacao) {
        Prioridade prioridade = solicitacao.getPrioridade();
        LocalDateTime prazo = LocalDateTime.now().plus(prioridade.getDiasSLA(), ChronoUnit.DAYS);
        solicitacao.setPrazoSLA(prazo);
    }
    private void validarSolicitacao(Solicitacao solicitacao) {
        if (solicitacao.getDescricao() == null || solicitacao.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }
    }
    private void registrarHistoricoInicial(Solicitacao solicitacao) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setSolicitacao(solicitacao);
        historico.setStatusNovo(Status.ABERTO);
        historico.setMotivo("Solicitação criada");
        historicoStatusRepository.save(historico);
    }
    private void registrarHistoricoTransicao(Solicitacao solicitacao, Status anterior, Status novo, Usuario responsavel, String motivo) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setSolicitacao(solicitacao);
        historico.setStatusAnterior(anterior);
        historico.setStatusNovo(novo);
        historico.setResponsavel(responsavel);
        historico.setMotivo(motivo);
        historicoStatusRepository.save(historico);
    }
}