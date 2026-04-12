package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {
    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa findById(Long id) {
        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> new RuntimeException("Empresa não encontrada com id: " + id));
        return empresa;
    }
}
