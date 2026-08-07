package dev.jhonjhonnys.estudos_spring.service;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jhonjhonnys.estudos_spring.model.Usuario;
import dev.jhonjhonnys.estudos_spring.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    
    //MOCKS -> Criam instancias "falsas" de um objeto
    //INJECTMOCKS -> Sinaliza ao mockito a qual classe devem ser 
    // injetados os mocks
    //INJECAO DE DEPENDENCIAS -> Previne com que os objetos sejam 
    // previamente instanciados na classe a utilizá-los
    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    @DisplayName("Deve cadastrar usuario com sucesso quando o e-mail nao estiver cadastrado")
    void deveCriarUsuarioComSucesso(){
        // Arrange (Dado que...)
        Usuario usuarioEntrada = new Usuario("John", "johnjhon@email.com");
        Usuario usuarioSalvo = new Usuario(1L, "John", "johnjhon@email.com");

        Mockito.when(repository.existsByEmail("johnjhon@email.com")).thenReturn(false);
        Mockito.when(repository.save(usuarioEntrada)).thenReturn(usuarioSalvo);
    
        // Act (Quando...)
        Usuario resultado = service.cadastrar(usuarioEntrada);

        // Assert (Entao...)
        assertThat(resultado)
                .isNotNull()
                .extracting("nome")
                .isEqualTo("John");

        //Verifica se os metodos do repository foram realmente chamados
        verify(repository, times(1)).existsByEmail("johnjhon@email.com");
        verify(repository, times(1)).save(usuarioEntrada);
    }
}
