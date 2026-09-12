package dev.jhonjhonnys.estudos_spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
import dev.jhonjhonnys.estudos_spring.exception.usuario.consts.ExceptionConstants;
import dev.jhonjhonnys.estudos_spring.model.Usuario;
import dev.jhonjhonnys.estudos_spring.service.UsuarioService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody @Valid UsuarioRequestDTO request){
        UsuarioResponseDTO dto = usuarioService.cadastrar(request);
        Usuario salvo = new Usuario(dto.id(), dto.nome(), dto.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleEntityExists(EntityExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ExceptionConstants.EMAIL_JA_CADASTRADO);
    }

    @GetMapping()
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email){
        try{
            UsuarioResponseDTO response = usuarioService.buscarPorEmail(email);
            Usuario usuarioEncontrado = new Usuario(response.id(),response.nome(),response.email());
            return ResponseEntity.status(HttpStatus.FOUND).body(usuarioEncontrado);
        }
        catch(Exception e){
            throw new EntityNotFoundException();
        }
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
