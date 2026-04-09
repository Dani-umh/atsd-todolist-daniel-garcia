package todolist.controller;

import todolist.authentication.ManagerUserSession;
import todolist.dto.UsuarioData;
import todolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void servicioLoginUsuarioOK() throws Exception {
        UsuarioData anaGarcia = new UsuarioData();
        anaGarcia.setNombre("Ana García");
        anaGarcia.setId(1L);

        when(usuarioService.login("ana.garcia@gmail.com", "12345678"))
                .thenReturn(UsuarioService.LoginStatus.LOGIN_OK);

        when(usuarioService.findByEmail("ana.garcia@gmail.com"))
                .thenReturn(anaGarcia);

        this.mockMvc.perform(post("/login")
                        .param("eMail", "ana.garcia@gmail.com")
                        .param("password", "12345678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/1/tareas"));
    }

    @Test
    public void servicioLoginAdministradorRedirigeAListaUsuarios() throws Exception {
        UsuarioData admin = new UsuarioData();
        admin.setNombre("Admin");
        admin.setId(1L);
        admin.setAdmin(true);

        when(usuarioService.login("admin@admin.com", "1234"))
                .thenReturn(UsuarioService.LoginStatus.LOGIN_OK);

        when(usuarioService.findByEmail("admin@admin.com"))
                .thenReturn(admin);

        this.mockMvc.perform(post("/login")
                        .param("eMail", "admin@admin.com")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registered"));
    }

    @Test
    public void servicioLoginUsuarioNotFound() throws Exception {
        when(usuarioService.login("pepito.perez@gmail.com", "12345678"))
                .thenReturn(UsuarioService.LoginStatus.USER_NOT_FOUND);

        this.mockMvc.perform(post("/login")
                        .param("eMail", "pepito.perez@gmail.com")
                        .param("password", "12345678"))
                .andExpect(content().string(containsString("No existe usuario")));
    }

    @Test
    public void servicioLoginUsuarioErrorPassword() throws Exception {
        when(usuarioService.login("ana.garcia@gmail.com", "000"))
                .thenReturn(UsuarioService.LoginStatus.ERROR_PASSWORD);

        this.mockMvc.perform(post("/login")
                        .param("eMail", "ana.garcia@gmail.com")
                        .param("password", "000"))
                .andExpect(content().string(containsString("Contraseña incorrecta")));
    }

    @Test
    public void servicioLoginUsuarioDeshabilitado() throws Exception {
        when(usuarioService.login("ana.garcia@gmail.com", "12345678"))
                .thenReturn(UsuarioService.LoginStatus.USER_DISABLED);

        this.mockMvc.perform(post("/login")
                        .param("eMail", "ana.garcia@gmail.com")
                        .param("password", "12345678"))
                .andExpect(content().string(containsString("Usuario deshabilitado")));
    }

    @Test
    public void listadoUsuariosRegistradosDevuelvePaginaConUsuarios() throws Exception {
        UsuarioData admin = new UsuarioData();
        admin.setId(99L);
        admin.setEmail("admin@umh.es");
        admin.setAdmin(true);

        UsuarioData usuario1 = new UsuarioData();
        usuario1.setId(1L);
        usuario1.setEmail("richard@umh.es");
        usuario1.setEnabled(true);

        UsuarioData usuario2 = new UsuarioData();
        usuario2.setId(2L);
        usuario2.setEmail("ada@umh.es");
        usuario2.setEnabled(false);

        when(managerUserSession.usuarioLogeado())
                .thenReturn(99L);

        when(usuarioService.findById(99L))
                .thenReturn(admin);

        when(usuarioService.allUsuarios())
                .thenReturn(Arrays.asList(usuario1, usuario2));

        this.mockMvc.perform(get("/registered"))
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString("richard@umh.es"),
                        containsString("ada@umh.es"),
                        containsString("1"),
                        containsString("2"),
                        containsString("Disable"),
                        containsString("Enable")
                )));
    }

    @Test
    public void descripcionUsuarioDevuelvePaginaConDatosUsuarioSinPassword() throws Exception {
        UsuarioData admin = new UsuarioData();
        admin.setId(99L);
        admin.setEmail("admin@umh.es");
        admin.setNombre("Admin");
        admin.setAdmin(true);

        UsuarioData usuario = new UsuarioData();
        usuario.setId(1L);
        usuario.setEmail("richard@umh.es");
        usuario.setNombre("Richard Stallman");
        usuario.setPassword("1234");

        when(managerUserSession.usuarioLogeado())
                .thenReturn(99L);

        when(usuarioService.findById(99L))
                .thenReturn(admin);

        when(usuarioService.findById(1L))
                .thenReturn(usuario);

        this.mockMvc.perform(get("/registered/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString("User description"),
                        containsString("1"),
                        containsString("richard@umh.es"),
                        containsString("Richard Stallman")
                )))
                .andExpect(content().string(not(containsString("1234"))));
    }

    @Test
    public void administradorPuedeCambiarEstadoDeAccesoDeUsuario() throws Exception {
        UsuarioData admin = new UsuarioData();
        admin.setId(99L);
        admin.setEmail("admin@umh.es");
        admin.setAdmin(true);

        UsuarioData usuarioActualizado = new UsuarioData();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setEmail("richard@umh.es");
        usuarioActualizado.setEnabled(false);

        when(managerUserSession.usuarioLogeado())
                .thenReturn(99L);

        when(usuarioService.findById(99L))
                .thenReturn(admin);

        when(usuarioService.toggleEnabledUsuario(1L))
                .thenReturn(usuarioActualizado);

        this.mockMvc.perform(post("/registered/1/toggle-enabled"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registered"));
    }
}