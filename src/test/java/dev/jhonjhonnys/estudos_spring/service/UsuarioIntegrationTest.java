package dev.jhonjhonnys.estudos_spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
import dev.jhonjhonnys.estudos_spring.exception.usuario.consts.ExceptionConstants;

@SpringBootTest()
@Transactional
@ActiveProfiles("test") //Faz com que o spring leia o .properties no test/resources
public class UsuarioIntegrationTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UsuarioService service;

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso(){
        UsuarioResponseDTO salvo = service.cadastrar(new UsuarioRequestDTO("John","johnjhon@email.com"));
        entityManager.flush();
        UsuarioResponseDTO encontrado = service.buscarPorEmail("johnjhon@email.com");
        assertThat(salvo).isNotNull();
        assertThat(salvo.nome()).isEqualTo("John");
        assertThat(salvo.email()).isEqualTo("johnjhon@email.com");
        assertThat(salvo.email()).isEqualTo(encontrado.email());
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar criar usuario com email ja existente")
    void deveLancarExcecaoQuandoEmailJaExistir(){
        service.cadastrar(new UsuarioRequestDTO("John","johnjhon@email.com"));
        entityManager.flush();
        UsuarioRequestDTO novo = new UsuarioRequestDTO("Jhonnys", "johnjhon@email.com");
        assertThatThrownBy(() -> service.cadastrar(novo))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ExceptionConstants.EMAIL_JA_CADASTRADO);
    }
}
