package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.MaterialDTO;
import com.eseltech.appbackendatelie.DTO.NotificacaoDTO;
import com.eseltech.appbackendatelie.entity.*;
import com.eseltech.appbackendatelie.exceptions.ResourceNotFoundException;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    public List<Material> findAll() {
        List<Material> lista = materialRepository.findAll();

        if (lista.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum material encontrado");
        }

        return lista;
    }

    public Material findById(Integer id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material não encontrado com id: " + id));
        return material;
    }

    public void removerMaterial(Integer id) {
        materialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material não encontrado com id: " + id));
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

    @Transactional
    public Material atualizarMaterial(Integer id, MaterialDTO dto) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material não encontrado com id: " + id));
        Empresa empresa = empresaRepository.findById(dto.empresaId()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + dto.empresaId()));

        material.setNome(dto.nome());
        material.setEmpresa(empresa);
        material.setCategoria(dto.categoria());
        material.setDescricao(dto.descricao());
        material.setQtdEstoque(dto.qtdEstoque());
        material.setPreco(dto.preco());

        return materialRepository.save(material);
    }

    @Transactional
    public void reduzirEstoqueMateriais(Pedido pedido) {
        List<ProdutosPedido> listaProdutos = pedido.getListaProdutos();

        Map<Long, BigDecimal> materiaisParaReduzir = new HashMap<>();

        for (ProdutosPedido produtosPedido : listaProdutos) {
            Produto produto = produtosPedido.getProduto();

            for (MaterialProduto materialProduto : produto.getListaMateriais()) {
                Long materialId = materialProduto.getMaterial().getId();
                BigDecimal quantidadeTotal = materiaisParaReduzir.getOrDefault(materialId, BigDecimal.ZERO);
                quantidadeTotal = quantidadeTotal.add(materialProduto.getQuantidade().multiply(BigDecimal.valueOf(produtosPedido.getQtdProduto())));
                materiaisParaReduzir.put(materialId, quantidadeTotal);
            }
        }

        materiaisParaReduzir.forEach((materialId, quantidade) -> {
            Material material = materialRepository.findById(materialId)
                    .orElseThrow(() -> new ResourceNotFoundException("Material não encontrado com id: " + materialId));
            BigDecimal novoEstoque = material.getQtdEstoque().subtract(quantidade);
            if (novoEstoque.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException("Estoque insuficiente para o material: " + material.getNome());
            }

            if (novoEstoque.compareTo(BigDecimal.valueOf(10.0)) <= 0) {

                Notificacao notificacao = new Notificacao();
                notificacao.setMensagem("Estoque do material " + material.getNome() + " está baixo: " + novoEstoque);
                notificacao.setTopico("Alerta de Estoque");
                notificacao.setEmpresa(material.getEmpresa());

                notificacaoService.salvarNotificacao(notificacao);
            }

            material.setQtdEstoque(novoEstoque);
            materialRepository.save(material);
        });
    }
}
