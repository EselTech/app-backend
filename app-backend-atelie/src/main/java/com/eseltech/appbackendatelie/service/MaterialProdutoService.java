package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.MaterialProduto;
import com.eseltech.appbackendatelie.repository.MaterialProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialProdutoService {
    @Autowired
    private MaterialProdutoRepository materialProdutoRepository;

    public MaterialProduto findById(Long id) {
        MaterialProduto materialProduto = materialProdutoRepository.findById(id).orElseThrow(() -> new RuntimeException("MaterialProduto não encontrado com id: " + id));
        return materialProduto;
    }
}
