package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.PedidoDTO;
import com.eseltech.appbackendatelie.DTO.ProdutosPedidoDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Pedido;
import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.entity.ProdutosPedido;
import com.eseltech.appbackendatelie.exceptions.ResourceNotFoundException;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.PedidoRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Pedido> findAll() {
        List<Pedido> lista = pedidoRepository.findAll();

        if (lista.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum pedido encontrado");
        }

        return lista;
    }

    public Pedido findById(Integer id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
    }

    public void removerPedido(Integer id) {
        pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
        pedidoRepository.deleteById(id);
    }

    @Transactional
    public Pedido salvarPedido(PedidoDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.empresaId().longValue()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + dto.empresaId()));

        Pedido pedido = new Pedido();
        pedido.setEmpresa(empresa);
        pedido.setNome(dto.nome());
        pedido.setDescricao(dto.descricao());
        pedido.setValor(dto.valor());
        pedido.setStatus(dto.status());
        pedido.setPrazo(dto.prazo());

        pedido.setListaProdutos(new ArrayList<>());

        if (dto.listaProdutos() != null && !dto.listaProdutos().isEmpty()) {
            processarProdutos(dto, pedido);
        }

        return pedidoRepository.save(pedido);
    }

    private void processarProdutos(PedidoDTO dto, Pedido pedido) {
        for (ProdutosPedidoDTO ppDTO : dto.listaProdutos()) {
            Produto produto = produtoRepository.findById(ppDTO.produtoId().longValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + ppDTO.produtoId()));

            ProdutosPedido produtosPedido = new ProdutosPedido();
            produtosPedido.setPedido(pedido);
            produtosPedido.setProduto(produto);
            produtosPedido.setQtdProduto(ppDTO.qtdProduto());

            pedido.getListaProdutos().add(produtosPedido);
        }
    }

    @Transactional
    public Pedido atualizarPedido(Integer id, PedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
        Empresa empresa = empresaRepository.findById(dto.empresaId().longValue()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + dto.empresaId()));

        pedido.setEmpresa(empresa);
        pedido.setNome(dto.nome());
        pedido.setDescricao(dto.descricao());
        pedido.setValor(dto.valor());
        pedido.setStatus(dto.status());
        pedido.setPrazo(dto.prazo());

        pedido.getListaProdutos().clear();

        if (dto.listaProdutos() != null && !dto.listaProdutos().isEmpty()) {
            processarProdutos(dto, pedido);
        }

        return pedidoRepository.save(pedido);
    }
}


