package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.HistoricoImpostoDTO;
import com.eseltech.appbackendatelie.DTO.MaterialProdutoDTO;
import com.eseltech.appbackendatelie.DTO.ProdutoDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.entity.MaterialProduto;
import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SimulacaoService simulacaoService;

    @Autowired
    private ImpostoService impostoService;


    public List<Produto> findAll() {
        List<Produto> lista = produtoRepository.findAll();

        if (lista.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum produto encontrado");
        }

        return lista;
    }

    public Produto findById(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));

        return produto;
    }

    public void removerProduto(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));

        produtoRepository.deleteById(id);
    }

    @Transactional
    public Produto salvarProduto(ProdutoDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.empresaId()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + dto.empresaId()));

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setEmpresa(empresa);
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());

        produto.setListaMateriais(new ArrayList<>());

        BigDecimal custoMateriais = processarMateriais(dto, produto);

        ValoresCalculados valores = calcularPrecificacao(custoMateriais, dto.custoMaoDeObra(), dto.margemLucroPercentual());

        produto.setCusto(valores.custoTotalComImpostos());
        produto.setPreco(valores.precoSugeridoDeVenda());

        return produtoRepository.save(produto);
    }

    private ValoresCalculados calcularPrecificacao(BigDecimal custoMateriais, BigDecimal custoMaoDeObra, BigDecimal margemLucroPercentual) {

        BigDecimal custoMaoDeObraFinal = (custoMaoDeObra != null) ? custoMaoDeObra : BigDecimal.ZERO;
        BigDecimal margemLucroFinal = (margemLucroPercentual != null) ? margemLucroPercentual : BigDecimal.ZERO;

        BigDecimal custoTotalBase = custoMateriais.add(custoMaoDeObraFinal);

        BigDecimal taxaIpca = BigDecimal.ZERO;
        try {
            List<HistoricoImpostoDTO> impostos = impostoService.buscarTodos();
            if (impostos != null && !impostos.isEmpty()) {
                taxaIpca = impostos.stream()
                        .filter(i -> i.nomeImposto().contains("IPCA"))
                        .map(HistoricoImpostoDTO::valor)
                        .findFirst()
                        .orElse(BigDecimal.ZERO);
            }
        } catch (Exception e) {
        }

        BigDecimal multiplicadorImposto = taxaIpca.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal valorImpostos = custoTotalBase.multiply(multiplicadorImposto);

        BigDecimal custoTotalComImpostos = custoTotalBase.add(valorImpostos);

        BigDecimal multiplicadorLucro = margemLucroFinal.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP).add(BigDecimal.ONE);
        BigDecimal precoSugeridoDeVenda = custoTotalComImpostos.multiply(multiplicadorLucro);

        return new ValoresCalculados(
                custoTotalComImpostos.setScale(2, RoundingMode.HALF_UP),
                precoSugeridoDeVenda.setScale(2, RoundingMode.HALF_UP)
        );
    }

    private record ValoresCalculados(BigDecimal custoTotalComImpostos, BigDecimal precoSugeridoDeVenda) {}

    private BigDecimal processarMateriais(ProdutoDTO dto, Produto produto) {
        BigDecimal custoMateriais = BigDecimal.ZERO;

        for (MaterialProdutoDTO mpDTO : dto.materiais()) {
            Material material = materialRepository.findById(mpDTO.materialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Material não encontrado"));

            MaterialProduto materialProduto = new MaterialProduto();
            materialProduto.setProduto(produto);
            materialProduto.setMaterial(material);
            materialProduto.setQuantidade(mpDTO.quantidade());

            BigDecimal custoDesteMaterial = material.getPreco()
                    .divide(material.getQtdEstoque(), 4, RoundingMode.HALF_UP)
                    .multiply(mpDTO.quantidade());

            custoMateriais = custoMateriais.add(custoDesteMaterial);
            produto.getListaMateriais().add(materialProduto);
        }

        return custoMateriais;
    }

    @Transactional
    public Produto salvarProdutoSimplificado(Long empresaId, String nome, String descricao,
                                              BigDecimal custo, BigDecimal preco) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + empresaId));

        Produto produto = new Produto();
        produto.setEmpresa(empresa);
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setCusto(custo);
        produto.setPreco(preco);
        produto.setListaMateriais(new ArrayList<>());

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto salvarProdutoShopee(Long empresaId, String nome, String descricao, BigDecimal preco) {
        logger.info("salvarProdutoShopee chamado - Nome: {}, Preço recebido: {}", nome, preco);
        
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + empresaId));

        BigDecimal precoFinal = (preco != null && preco.compareTo(BigDecimal.ZERO) > 0) ? preco : BigDecimal.ZERO;
        BigDecimal custoEstimado = precoFinal.multiply(new BigDecimal("0.8"));

        logger.info("Valores calculados - Preço final: {}, Custo estimado: {}", precoFinal, custoEstimado);

        Produto produto = new Produto();
        produto.setEmpresa(empresa);
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setCusto(custoEstimado);
        produto.setPreco(precoFinal);
        produto.setListaMateriais(new ArrayList<>());

        Produto produtoSalvo = produtoRepository.save(produto);
        logger.info("Produto salvo - ID: {}, Preço: {}, Custo: {}", 
                   produtoSalvo.getId(), produtoSalvo.getPreco(), produtoSalvo.getCusto());

        return produtoSalvo;
    }

    @Transactional
    public Produto atualizarProduto(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
        Empresa empresa = empresaRepository.findById(dto.empresaId()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + dto.empresaId()));

        produto.setNome(dto.nome());
        produto.setEmpresa(empresa);
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());

        produto.getListaMateriais().clear();
        BigDecimal custoMateriais = BigDecimal.ZERO;

        processarMateriais(dto, produto, custoMateriais);

        ValoresCalculados valores = calcularPrecificacao(custoMateriais, dto.custoMaoDeObra(), dto.margemLucroPercentual());

        produto.setCusto(valores.custoTotalComImpostos());
        produto.setPreco(valores.precoSugeridoDeVenda());

        return produtoRepository.save(produto);
    }
}
