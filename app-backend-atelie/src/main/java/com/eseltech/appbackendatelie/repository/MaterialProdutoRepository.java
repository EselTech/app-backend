package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.MaterialProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialProdutoRepository extends JpaRepository<MaterialProduto, Long> {
}
