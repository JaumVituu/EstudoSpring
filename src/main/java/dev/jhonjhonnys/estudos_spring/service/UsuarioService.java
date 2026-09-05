package dev.jhonjhonnys.estudos_spring.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
import dev.jhonjhonnys.estudos_spring.exception.usuario.UserMismatchException;
import dev.jhonjhonnys.estudos_spring.exception.usuario.consts.ExceptionConstants;
import dev.jhonjhonnys.estudos_spring.model.Usuario;
import dev.jhonjhonnys.estudos_spring.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto){
        if (repository.existsByEmail(dto.email())){
            throw new IllegalArgumentException(ExceptionConstants.EMAIL_JA_CADASTRADO);
        }

        Usuario novo = new Usuario(dto.nome(),dto.email());
        Usuario salvo = repository.save(novo);
        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    public UsuarioResponseDTO buscarPorEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.USUARIO_NAO_ENCONTRADO));
    }

    public UsuarioResponseDTO deletarPorId(Long id){
        Optional<Usuario> usuarioOpt = repository.findById(id);
        if(usuarioOpt.isEmpty()){
            throw new EntityNotFoundException(ExceptionConstants.USUARIO_NAO_ENCONTRADO);
        }
        Usuario usuario =  usuarioOpt.get();
        repository.deleteById(id);
        return new UsuarioResponseDTO (usuario.getId(),usuario.getNome(),usuario.getEmail());
    }

    public UsuarioResponseDTO editar(Long id, String novoNome, String novoEmail) {
        Usuario alterado = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.USUARIO_NAO_ENCONTRADO));
        alterado.setNome(novoNome);
        alterado.setEmail(novoEmail);
        Usuario salvo = repository.save(alterado);
        if(!alterado.equals(salvo)){
            throw new UserMismatchException(ExceptionConstants.DADOS_NAO_COINCIDEM);
        }
        return new UsuarioResponseDTO(salvo.getId(),salvo.getNome(),salvo.getEmail());
    }

    public UsuarioResponseDTO editarNome(Long id, String novoNome) {
        Usuario alterado = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.USUARIO_NAO_ENCONTRADO));
        alterado.setNome(novoNome);
        Usuario salvo = repository.save(alterado);
        if(!alterado.equals(salvo)){
            throw new UserMismatchException(ExceptionConstants.DADOS_NAO_COINCIDEM);
        }
        return new UsuarioResponseDTO(salvo.getId(),salvo.getNome(),salvo.getEmail());
    }

    public UsuarioResponseDTO editarEmail(Long id, String novoEmail) {
        Usuario alterado = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.USUARIO_NAO_ENCONTRADO));
        alterado.setEmail(novoEmail);
        Usuario salvo = repository.save(alterado);
        if(!alterado.equals(salvo)){
            throw new UserMismatchException(ExceptionConstants.DADOS_NAO_COINCIDEM);
        }
        return new UsuarioResponseDTO(salvo.getId(),salvo.getNome(),salvo.getEmail());
    }
}
