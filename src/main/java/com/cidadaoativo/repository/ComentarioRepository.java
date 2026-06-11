package com.cidadaoativo.repository;
import com.cidadaoativo.entity.Comentario;
import com.cidadaoativo.entity.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findBySolicitacao(Solicitacao solicitacao);
}