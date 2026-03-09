package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Imposto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * Busca impostos cujo nome contenha o texto especificado (case-insensitive).
     *
     * @param nome Nome ou parte do nome do imposto a ser buscado
     * @return Lista de impostos que correspondem à busca
     */
    public List<Imposto> findByNomeImpostoContainingIgnoreCase(String nome);
}
