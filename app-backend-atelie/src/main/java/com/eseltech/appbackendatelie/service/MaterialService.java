package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.MaterialDTO;
import com.eseltech.appbackendatelie.DTO.ProdutoDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Material> findAll() {
        List<Material> lista = materialRepository.findAll();

        if (lista.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum material encontrado");
        }

        return lista;
    }

    public Material findById(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new RuntimeException("Material não encontrado com id: " + id));
        return material;
    }

    public void removerMaterial(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material não encontrado com id: " + id));

        materialRepository.deleteById(id);
    }

    @Transactional
    public Material salvarMaterial(MaterialDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.empresaId()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + dto.empresaId()));

        Material material = new Material();
        material.setNome(dto.nome());
        material.setEmpresa(empresa);
        material.setCategoria(dto.categoria());
        material.setDescricao(dto.descricao());
        material.setQtdEstoque(dto.qtdEstoque());
        material.setPreco(dto.preco());

        return materialRepository.save(material);
    }
}
