package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.OrcamentoDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Orcamento;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.OrcamentoRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;


    public List<Orcamento> findAll() {
        List<Orcamento> lista = repository.findAll();

        if (lista.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum produto encontrado");
        }

        return lista;
    }

    public Orcamento findById(Long id) {
        Orcamento orcamento = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Orcamento não encontrado com id: " + id));

        return orcamento;
    }

    @Transactional
    public Orcamento salvaOrcamento(OrcamentoDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.empresaId()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + dto.empresaId()));

        Orcamento orcamento = new Orcamento();
        orcamento.setTitulo(dto.titulo());
        orcamento.setEmpresa(empresa);
        orcamento.setCliente(dto.cliente());
        orcamento.setValor(dto.valor());

        return repository.save(orcamento);
    }

    @Transactional
    public Orcamento atualizarOrcamento(Long id, OrcamentoDTO dto) {
        Orcamento orcamento = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Orcamento não encontrado com id: " + id));
        Empresa empresa = empresaRepository.findById(dto.empresaId()).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + dto.empresaId()));

        orcamento.setTitulo(dto.titulo());
        orcamento.setEmpresa(empresa);
        orcamento.setCliente(dto.cliente());
        orcamento.setValor(dto.valor());

        return repository.save(orcamento);
    }

    public void removerOrcamento(Long id) {
        Orcamento orcamento = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));

        repository.deleteById(id);
    }
}
