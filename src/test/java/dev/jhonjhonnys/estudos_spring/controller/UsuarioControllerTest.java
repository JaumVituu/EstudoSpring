package dev.jhonjhonnys.estudos_spring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// Importa post(), get(), put(), delete(), etc.
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// Importa status(), jsonPath(), content(), etc.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
import dev.jhonjhonnys.estudos_spring.model.Usuario;
import dev.jhonjhonnys.estudos_spring.service.UsuarioService;
import tools.jackson.databind.ObjectMapper;

// Testa camada Controller isoladamente do Model - View - Controller
@WebMvcTest(UsuarioController.class)
@WithMockUser // Simula um usuario autenticado
public class UsuarioControllerTest {

    //Autowired automaticamente injeta dependencias
    // que tem anotacao bean
    @Autowired
    private MockMvc mockMvc;

    //ObjectMapper converte JSON em String
    @Autowired
    ObjectMapper objectMapper;
    
    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve criar uwm usuário com sucesso e retornar status 201 Created")
    void deveCriarUsuarioComSucesso() throws Exception{
        //Arrange (Dado que...)
        UsuarioRequestDTO request = new UsuarioRequestDTO("John", "johnjhon@email.com");
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "John", "johnjhon@email.com");
        
        //Assert (Quando...)
        when(usuarioService.cadastrar(any(UsuarioRequestDTO.class))).thenReturn(response);

        //Act (Entao...)
        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("John"))
                .andExpect(jsonPath("$.email").value("johnjhon@email.com"));
        verify(usuarioService).cadastrar(any(UsuarioRequestDTO.class));
    }
}
