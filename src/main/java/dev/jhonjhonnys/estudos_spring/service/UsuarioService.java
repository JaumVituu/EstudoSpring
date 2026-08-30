package dev.jhonjhonnys.estudos_spring.service;

import java.util.Optional;

import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
import dev.jhonjhonnys.estudos_spring.exception.usuario.userMismatchException;
import dev.jhonjhonnys.estudos_spring.model.Usuario;
import dev.jhonjhonnys.estudos_spring.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

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
        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    public UsuarioResponseDTO buscarPorEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));
    }

    public UsuarioResponseDTO deletarPorId(Long id){
        Optional<Usuario> usuarioOpt = repository.findById(id);
        if(usuarioOpt.isEmpty()){
            throw new EntityNotFoundException("Nenhum usuario encontrado com este Id");
        }
        Usuario usuario =  usuarioOpt.get();
        repository.deleteById(id);
        return new UsuarioResponseDTO (usuario.getId(),usuario.getNome(),usuario.getEmail());
    }

    public UsuarioResponseDTO editarNomeEEmail(Long id, String novoNome, String novoEmail) {
        Usuario alterado = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nenhum usuario encontrado com este Id"));
        alterado.setNome(novoNome);
        alterado.setEmail(novoEmail);
        Usuario salvo = repository.save(alterado);
        if(!alterado.equals(salvo)){
            throw new userMismatchException("Dados inseridos nao coincidem com usuario persistido");
        }
        UsuarioResponseDTO resposta = new UsuarioResponseDTO(salvo.getId(),salvo.getNome(),salvo.getEmail());
        return resposta;
    }
}
