package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.home.HomeResponseDTO;
import com.eseltech.appbackendatelie.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@Tag(name = "Home Dashboard", description = "Endpoints para o resumo financeiro da Home")
public class HomeController {

    @Autowired
    private HomeService service;

    @GetMapping("/{empresaId}")
    @Operation(summary = "Resumo Financeiro", description = "Retorna os indicadores financeiros (KPIs) para a home do ateliê")
    public ResponseEntity<HomeResponseDTO> getHomeData(@PathVariable Integer empresaId) {
        return ResponseEntity.ok(service.getHomeData(empresaId));
    }
}