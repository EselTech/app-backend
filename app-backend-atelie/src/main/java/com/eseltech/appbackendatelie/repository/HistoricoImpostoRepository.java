package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.HistoricoImposto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoImpostoRepository extends JpaRepository<HistoricoImposto, Long> {
}
