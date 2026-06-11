package com.cidadaoativo.repository;
import com.cidadaoativo.entity.HistoricoStatus;
import com.cidadaoativo.entity.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {
    List<HistoricoStatus> findBySolicitacao(Solicitacao solicitacao);
}