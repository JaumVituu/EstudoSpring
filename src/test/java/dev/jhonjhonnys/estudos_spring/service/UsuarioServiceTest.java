package dev.jhonjhonnys.estudos_spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
import dev.jhonjhonnys.estudos_spring.exception.usuario.userMismatchException;
import dev.jhonjhonnys.estudos_spring.model.Usuario;
import dev.jhonjhonnys.estudos_spring.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    
    //MOCKS -> Criam instancias "falsas" de um objeto
    //INJECTMOCKS -> Sinaliza ao mockito a qual classe devem ser 
    // injetados os mocks
    //INJECAO DE DEPENDENCIAS -> Previne com que os objetos sejam 
    // previamente instanciados na classe a utilizá-los

    //PADRAO TDD
    //Arrange (Dado que...)
    //Act (Quando...)
    //Assert (Então)
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
        when(repository.existsByEmail(usuarioExistente.email())).thenReturn(true);
        
        //Assert (Entao...)
        assertThatThrownBy(() -> service.cadastrar(usuarioExistente))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email já cadastrado");
    }

    
    @Test
    @DisplayName("deve encontrar usuario existente por meio de e-mail")
    void deveEncontrarPorEmail(){
        //Arrange (dado que...)
        String email = "johnjhon@email.com";
        UsuarioResponseDTO saidaDTO = new UsuarioResponseDTO(1L, "John", "johnjhon@email.com");
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(saidaDTO));
        
        //Act (quando...)
        UsuarioResponseDTO response = service.buscarPorEmail(email);

        //Assert (entao...)
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("John");
        assertThat(response.email()).isEqualTo("johnjhon@email.com");
    }

    @Test
    @DisplayName("deve lancar excecao ao tentar buscar por um email inexistente")
    void deveLancarExcecaoQuandoBuscaEmailInexistente(){
        //Arrange (Dado que...)
        String email = "johnjhon@email.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        //Assert (Entao...)
        assertThatThrownBy(() -> service.buscarPorEmail(email))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Usuario nao encontrado");
    }

    @Test
    @DisplayName("deve deletar usuario existente por meio de id")
    void deveDeletarUsuarioPorId(){
        //Arrange (Dado que...)
        Long idUsuario = 1L;
        Usuario saida = new Usuario(idUsuario, "John", "johnjhon@email.com");
        Mockito.when(repository.findById(idUsuario)).thenReturn(Optional.of(saida));

        //Act (Quando...)
        UsuarioResponseDTO response = service.deletarPorId(idUsuario);

        //Assert (entao...)
        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo("John");
        assertThat(response.id()).isEqualTo(idUsuario);
    }

    @Test
    @DisplayName("deve lancar excecao ao tentar deletar usuario inexistente via id")
    void deveLancarExcecaoQuandoDeleteIdInexistente(){
        //Arrange(Dado que...)
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        //Act e Assert (Quando e Entao)
        assertThatThrownBy(() -> service.deletarPorId(id))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Nenhum usuario encontrado com este Id");
    }

    @Test
    @DisplayName("deve editar nome e email do usuario")
    void deveEditarNomeEEmail(){
        Usuario existente = new Usuario(1L,"John", "johnjhon@email.com");
        Long id = 1L;
        String novoEmail = "jhonnys@email.com";
        String novoNome = "John Jhon";
        Usuario alterado = new Usuario(id, novoNome, novoEmail);
        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(alterado)).thenReturn(alterado);

        UsuarioResponseDTO response = service.editarNomeEEmail(id, novoNome, novoEmail);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(alterado.getId());
        assertThat(response.nome()).isEqualTo(alterado.getNome());
        assertThat(response.email()).isEqualTo(alterado.getEmail());
    }

    @Test
    @DisplayName("deve lancar excecao ao tentar editar nome e email de usuario inexistente via id")
    void deveLancarExcecaoQuandoEditarNomeEEmailInexistente(){
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editarNomeEEmail(id, null, null))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Nenhum usuario encontrado com este Id");
    }

    @Test
    @DisplayName("deve lancar excecao quando nao salvar alteracao de nome e email")
    void deveLancarExcecaoQuandoNaoSalvarNomeEEmail(){
        String novoNome = "Jhonnys";
        String novoEmail = "jhonnys@email.com";
        Long id = 1L;
        Usuario antigo = new Usuario(id, "John", "johnjhon@email.com");
        Usuario novo = new Usuario(id, novoNome, novoEmail);
        when(repository.findById(id)).thenReturn(Optional.of(antigo));
        when(repository.save(novo)).thenReturn(any(Usuario.class));

        assertThatThrownBy(() -> service.editarNomeEEmail(id, novoNome, novoEmail))
            .isInstanceOf(userMismatchException.class)
            .hasMessage("Dados inseridos nao coincidem com usuario persistido");
    }
}
