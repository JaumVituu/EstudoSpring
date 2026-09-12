// package dev.jhonjhonnys.estudos_spring.controller;

// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// // Importa post(), get(), put(), delete(), etc.
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// // Importa status(), jsonPath(), content(), etc.
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioRequestDTO;
// import dev.jhonjhonnys.estudos_spring.dto.usuario.UsuarioResponseDTO;
// import dev.jhonjhonnys.estudos_spring.exception.usuario.consts.ExceptionConstants;
// import dev.jhonjhonnys.estudos_spring.model.Usuario;
// import dev.jhonjhonnys.estudos_spring.service.UsuarioService;
// import jakarta.persistence.EntityExistsException;
// import jakarta.persistence.EntityNotFoundException;
// import tools.jackson.databind.ObjectMapper;

// // Testa camada Controller isoladamente do Model - View - Controller
// @WebMvcTest(UsuarioController.class)
// @WithMockUser // Simula um usuario autenticado
// class UsuarioControllerTest {

//     //Autowired automaticamente injeta dependencias
//     // que tem anotacao bean
//     @Autowired
//     private MockMvc mockMvc;

//     //ObjectMapper converte JSON em String
//     @Autowired
//     ObjectMapper objectMapper;
    
//     @MockitoBean
//     private UsuarioService service;

//     @Test
//     @DisplayName("Deve criar um usuário com sucesso e retornar status 201 Created")
//     void deveCriarUsuarioComSucesso() throws Exception{
//         //Arrange (Dado que...)
//         UsuarioRequestDTO request = new UsuarioRequestDTO("John", "johnjhon@email.com");
//         UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "John", "johnjhon@email.com");
        
//         //Act (Quando...)
//         when(service.cadastrar(any(UsuarioRequestDTO.class))).thenReturn(response);

//         //Assert (Entao...)
//         mockMvc.perform(post("/usuarios")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(request))
//                 .with(csrf()))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id").value(1L))
//                 .andExpect(jsonPath("$.nome").value("John"))
//                 .andExpect(jsonPath("$.email").value("johnjhon@email.com"));
//         verify(service).cadastrar(any(UsuarioRequestDTO.class));
//     }

//     @Test
//     @DisplayName("Deve lancar excecao ao tentar criar um usuario com email ja cadastrado")
//     void deveLancarExcecaoQuandoCadastraEmailExistente() throws Exception{
//         String email = "johnjhon@email.com";
//         Usuario novo = new Usuario("John", email);

//         when(service.cadastrar(new UsuarioRequestDTO(novo.getNome(), email))).thenThrow(new EntityExistsException(ExceptionConstants.EMAIL_JA_CADASTRADO));

//         mockMvc.perform(post("/usuarios")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(novo))
//                 .with(csrf()))
//                 .andExpect(status().isConflict())
//                 .andExpect(content().string(ExceptionConstants.EMAIL_JA_CADASTRADO));
//     }

//     @Test
//     @DisplayName("Deve buscar um usuário por email com sucesso e retornar status x")
//     void deveBuscarPorEmailComSucesso() throws Exception{
//         //Arrange (Dado que...)
//         String email = "johnjhon@email.com";
//         Usuario existente = new Usuario(1L, "John", "johnjhon@email.com");

//         //Act (Quando...)
//         when(service.buscarPorEmail(email)).thenReturn(new UsuarioResponseDTO(existente.getId(),existente.getNome(),existente.getEmail()));

//         //Assert
//         mockMvc.perform(get("/usuarios?email=johnjhon@email.com")
//                 .with(csrf()))
//                 .andExpect(status().isFound())
//                 .andExpect(jsonPath("$.id").value(1L))
//                 .andExpect(jsonPath("$.nome").value("John"))
//                 .andExpect(jsonPath("$.email").value("johnjhon@email.com"));
//         verify(service).buscarPorEmail(email);
//     }
    
//     @Test 
//     @DisplayName("Deve lancar excecao ao buscar por um usuario nao existente")
//     void deveLancarExcecaoQuandoBuscaEmailInexistente() throws Exception{
//         String email = "johnjhon@email.com";
//         when(service.buscarPorEmail(email)).thenThrow(EntityNotFoundException.class);
//         mockMvc.perform(get("/usuarios?email="+email))
//             .andExpect(status().isNotFound())
//             .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
//     }
// }
