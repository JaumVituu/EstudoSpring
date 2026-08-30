package dev.jhonjhonnys.estudos_spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
import dev.jhonjhonnys.estudos_spring.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<UsuarioResponseDTO> findByEmail (String email);
    void deleteById(Long Id);
    boolean existsByEmail(String email);
}
