package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.dash.DashboardResponseDTO;
import com.eseltech.appbackendatelie.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(@RequestParam Long empresaId) {
        try {
            // Agora passamos o ID recebido na URL para o Service
            DashboardResponseDTO dadosDaDashboard = service.montarDashboard(empresaId);
            return ResponseEntity.ok(dadosDaDashboard);

        } catch (Exception e) {
            // Opcional: logar o erro para saber o que aconteceu no console
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}