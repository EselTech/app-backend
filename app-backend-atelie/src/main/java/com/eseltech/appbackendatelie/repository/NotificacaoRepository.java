package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    @Query("SELECT n FROM Notificacao n WHERE n.dtEnvio IS NULL")
    List<Notificacao> findNotificacoesAEnviar();
}

