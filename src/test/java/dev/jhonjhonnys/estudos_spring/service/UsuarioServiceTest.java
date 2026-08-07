package dev.jhonjhonnys.estudos_spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void deveCriarUsuarioComSucesso(){
        // Arrange (Dado que...)
        Usuario usuarioEntrada = new Usuario(null, "John", "johnjhon@email.com");
        Usuario usuarioSalvo = new Usuario(1L, "John", "johnjhon@email.com");

        Mockito.when(repository.existsByEmail("johnjhon@email.com").thenReturn(false));
        Mockito.when(repository.save(usuarioEntrada).thenReturn(usuarioSalvo));
    
        // Act (Quando...)
        Usuario resultado = service.cadastrar(usuarioEntrada);

        // Assert (Entao...)
        Assertions.assertNotNull(resultado.getId());
        Assertions.assertEquals("John", resultado.getNome());
    }
}
