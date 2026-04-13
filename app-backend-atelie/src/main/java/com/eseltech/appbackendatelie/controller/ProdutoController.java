package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.ProdutoDTO;
import com.eseltech.appbackendatelie.DTO.request.SimularPrecoRequestDTO;
import com.eseltech.appbackendatelie.DTO.response.SimularPrecoResponseDTO;
import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.service.EmpresaService;
import com.eseltech.appbackendatelie.service.MaterialService;
import com.eseltech.appbackendatelie.service.ProdutoService;
import com.eseltech.appbackendatelie.service.SimulacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private SimulacaoService simulacaoService;

    @GetMapping
    public ResponseEntity<List<Produto>> findAll() {
        return ResponseEntity.ok(produtoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(Long id) {
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(Long id) {
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/simular-preco")
    public ResponseEntity<SimularPrecoResponseDTO> simularPreco(@RequestBody @Valid SimularPrecoRequestDTO simularPrecoRequestDTO) {
        SimularPrecoResponseDTO response = simulacaoService.simularPreco(simularPrecoRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Produto> saveProduto(@RequestBody @Valid ProdutoDTO produto) {
        return ResponseEntity.ok(produtoService.salvarProduto(produto));
    }
}
