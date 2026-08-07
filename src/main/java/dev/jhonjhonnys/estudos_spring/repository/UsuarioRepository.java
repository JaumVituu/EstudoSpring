package dev.jhonjhonnys.estudos_spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jhonjhonnys.estudos_spring.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByEmail (String email);

    boolean existsByEmail(String email);
}
