package dev.jhonjhonnys.estudos_spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
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
        UsuarioRequestDTO dtoEntrada = new UsuarioRequestDTO(usuarioEntrada.getNome(),usuarioEntrada.getEmail());
        UsuarioResponseDTO dtoSaida = new UsuarioResponseDTO(1L, "John", "johnjhon@email.com");
        Usuario usuarioSalvo = new Usuario(dtoSaida.id(),dtoSaida.nome(),dtoSaida.email());

        Mockito.when(repository.existsByEmail("johnjhon@email.com")).thenReturn(false);
        Mockito.when(repository.save(any(Usuario.class))).thenReturn(usuarioSalvo);
    
        // Act (Quando...)
        UsuarioResponseDTO resultado = service.cadastrar(dtoEntrada);

        // Assert (Entao...)
        assertThat(resultado)
                .isNotNull()
                .extracting("nome")
                .isEqualTo("John");

        //Verifica se os metodos do repository foram realmente chamados
        verify(repository, times(1)).existsByEmail("johnjhon@email.com");
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("deve lancar excecao ao tentar cadastrar usuario com e-mail existente")
    void deveLancarExcecaoQuandoEmailJaExistir(){
        //Arrange (dado que...)
        UsuarioRequestDTO usuarioExistente = new UsuarioRequestDTO("John", "johnjhon@email.com");

        //Act (Quando...)
        when(repository.existsByEmail(usuarioExistente.email())).thenReturn(true);
        
        //Assert (Entao...)
        assertThatThrownBy(() -> service.cadastrar(usuarioExistente))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email já cadastrado");
    }
}
