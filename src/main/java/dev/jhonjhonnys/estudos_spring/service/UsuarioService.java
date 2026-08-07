package dev.jhonjhonnys.estudos_spring.service;

import dev.jhonjhonnys.estudos_spring.model.Usuario;
import dev.jhonjhonnys.estudos_spring.repository.UsuarioRepository;

public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    public Usuario cadastrar(Usuario usuario){
        if (repository.existsByEmail(usuario.getEmail())){
            throw new IllegalArgumentException("Email já cadastrado");
        }
        return repository.save(usuario);
    }
}
