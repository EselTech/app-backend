package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public Material findById(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new RuntimeException("Material não encontrado com id: " + id));
        return material;
    }
}
