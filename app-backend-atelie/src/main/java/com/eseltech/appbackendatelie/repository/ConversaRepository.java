package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Conversa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversaRepository extends JpaRepository<Conversa, Integer> {
    List<Conversa> findByEmpresaIdOrderByDtHoraConversaAsc(Integer empresaId);
}
