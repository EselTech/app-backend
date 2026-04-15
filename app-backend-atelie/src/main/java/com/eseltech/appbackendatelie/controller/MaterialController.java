package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.MaterialDTO;
import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiais")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public ResponseEntity<List<Material>> findAll() {
        return ResponseEntity.ok(materialService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getById(Long id) {
        return ResponseEntity.ok(materialService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(Long id) {
        materialService.removerMaterial(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Material> saveMaterial(@RequestBody @Valid MaterialDTO materialDTO) {
        return ResponseEntity.ok(materialService.salvarMaterial(materialDTO));
    }

}
