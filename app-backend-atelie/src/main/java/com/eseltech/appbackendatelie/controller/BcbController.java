package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.HistoricoImpostoDTO;
import com.eseltech.appbackendatelie.entity.Imposto;
import com.eseltech.appbackendatelie.service.ImpostoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bcb")
public class BcbController {
    @Autowired
    private ImpostoService impostoService;

    @GetMapping("/find-all")
    public ResponseEntity<List<HistoricoImpostoDTO>> getAll() {
        return ResponseEntity.ok(impostoService.buscarTodos());
    }

    @PostMapping("/inserir-imposto")
    public ResponseEntity<String> inserirImposto(@RequestBody Imposto imposto) {
        return ResponseEntity.ok(impostoService.inserirImposto(imposto));
    }

    @PostMapping("/atualizar-indicadores")
    public ResponseEntity<String> atualizarImpostos(){
        impostoService.atualizarImpostos();
        return ResponseEntity.ok("Atualizado com sucesso!");
    }
}