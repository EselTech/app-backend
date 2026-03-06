package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.HistoricoImposto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório JPA para operações de persistência da entidade {@link HistoricoImposto}.
 * Fornece métodos para consulta, inserção, atualização e exclusão de histórico de impostos.
 *
 * @author EselTech
 * @version 1.0.0
 * @since 2024
 */
@Repository
public interface HistoricoImpostoRepository extends JpaRepository<HistoricoImposto, Long> {

    /**
     * Busca um registro de histórico pelo ID do imposto.
     *
     * @param impostoId ID do imposto a ser consultado
     * @return Registro de histórico correspondente ao imposto
     */
    HistoricoImposto findByImpostoId(Integer impostoId);

    /**
     * Remove todos os registros de histórico associados a um imposto específico.
     *
     * @param imposto_id ID do imposto cujo histórico será removido
     */
    @Transactional
    @Modifying
    void deleteByImpostoId(Integer imposto_id);

    /**
     * Busca o registro de histórico mais recente de um imposto específico.
     *
     * @param idImposto ID do imposto a ser consultado
     * @return Registro de histórico mais recente do imposto
     */
    @NativeQuery("""
        SELECT * FROM historico_imposto hi WHERE hi.imposto_id = :idImposto ORDER BY hi.data_registro LIMIT 1
    """)
    public HistoricoImposto acharRecentePorImpostoId(Integer idImposto);

}
