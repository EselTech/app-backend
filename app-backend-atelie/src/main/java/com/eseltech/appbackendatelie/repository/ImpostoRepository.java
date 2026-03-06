package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Imposto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para operações de persistência da entidade {@link Imposto}.
 * Fornece métodos para consulta, inserção, atualização e exclusão de impostos.
 *
 * @author EselTech
 * @version 1.0.0
 * @since 2024
 */
@Repository
public interface ImpostoRepository extends JpaRepository<Imposto, Integer> {

    /**
     * Busca um imposto pelo seu código SGS do Banco Central.
     *
     * @param codigoSgs Código do Sistema Gerenciador de Séries do BCB
     * @return Imposto correspondente ao código SGS, ou null se não encontrado
     */
    public Imposto findByCodigoSgs(Integer codigoSgs);
}
