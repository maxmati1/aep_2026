package com.cidadaoativo.repository;
import com.cidadaoativo.entity.Solicitacao;
import com.cidadaoativo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    Optional<Solicitacao> findByProtocolo(String protocolo);
    List<Solicitacao> findByStatus(Status status);
    List<Solicitacao> findByBairro(String bairro);
}