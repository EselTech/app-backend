package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    <T> ScopedValue<T> findProdutoById(Integer id);
}
