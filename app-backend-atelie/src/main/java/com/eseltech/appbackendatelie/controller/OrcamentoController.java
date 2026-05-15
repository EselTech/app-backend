package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.OrcamentoDTO;
import com.eseltech.appbackendatelie.entity.Orcamento;
import com.eseltech.appbackendatelie.service.OrcamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    @Autowired
    private OrcamentoService service;

    @PostMapping
    public ResponseEntity<Orcamento> saveOrcamento(@RequestBody @Valid OrcamentoDTO orcamento) {
        return ResponseEntity.ok(service.salvaOrcamento(orcamento));
    }

    @GetMapping
    public ResponseEntity<List<Orcamento>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orcamento> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orcamento> updateProduto(@PathVariable Long id, @RequestBody @Valid OrcamentoDTO orcamento) {
        return ResponseEntity.ok(service.atualizarOrcamento(id, orcamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.removerOrcamento(id);
        return ResponseEntity.noContent().build();
    }
}
