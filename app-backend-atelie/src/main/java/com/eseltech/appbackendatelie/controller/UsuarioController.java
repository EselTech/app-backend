package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.Usuario;
import com.eseltech.appbackendatelie.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/find-all")
    public ResponseEntity<List<Usuario>> buscarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario usuario) {
        usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.ok().body("Cadastrado com sucesso!");
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarUsuario(@RequestBody Usuario usuario, @PathVariable Long id) {
        usuarioService.atualizarUsuario(usuario, id);
        return ResponseEntity.ok().body("Atualizado com sucesso!");
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<String> removerUsuario(@PathVariable Long id) {
        usuarioService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
