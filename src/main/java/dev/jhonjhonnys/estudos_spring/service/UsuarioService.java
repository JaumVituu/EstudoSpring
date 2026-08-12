package dev.jhonjhonnys.estudos_spring.service;

import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
import dev.jhonjhonnys.estudos_spring.model.Usuario;
import dev.jhonjhonnys.estudos_spring.repository.UsuarioRepository;

public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto){
        if (repository.existsByEmail(dto.email())){
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario novo = new Usuario(dto.nome(),dto.email());
        Usuario salvo = repository.save(novo);
        return new UsuarioResponseDTO(salvo.getId(), salvo.getEmail(), salvo.getNome());
    }
}
