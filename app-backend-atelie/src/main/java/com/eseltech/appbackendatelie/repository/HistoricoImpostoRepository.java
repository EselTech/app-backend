package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.HistoricoImposto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoImpostoRepository extends JpaRepository<HistoricoImposto, Long> {

    @NativeQuery("""
        SELECT * FROM historico_imposto hi WHERE hi.imposto_id = :idImposto ORDER BY hi.data_registro LIMIT 1
    """)
    public HistoricoImposto acharRecentePorImpostoId(Integer idImposto);

}
