package todolist.service;

import todolist.dto.UsuarioData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    Long addUsuarioBD() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("richard@umh.es");
        usuario.setNombre("Richard Stallman");
        usuario.setPassword("1234");
        UsuarioData nuevoUsuario = usuarioService.registrar(usuario);
        return nuevoUsuario.getId();
    }

    @Test
    public void servicioLoginUsuario() {
        addUsuarioBD();

        UsuarioService.LoginStatus loginStatus1 = usuarioService.login("richard@umh.es", "1234");
        UsuarioService.LoginStatus loginStatus2 = usuarioService.login("richard@umh.es", "0000");
        UsuarioService.LoginStatus loginStatus3 = usuarioService.login("ricardo.perez@gmail.com", "12345678");

        assertThat(loginStatus1).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);
        assertThat(loginStatus2).isEqualTo(UsuarioService.LoginStatus.ERROR_PASSWORD);
        assertThat(loginStatus3).isEqualTo(UsuarioService.LoginStatus.USER_NOT_FOUND);
    }

    @Test
    public void servicioRegistroUsuario() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");

        usuarioService.registrar(usuario);

        UsuarioData usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getEmail()).isEqualTo("usuario.prueba2@gmail.com");
    }

    @Test
    public void servicioRegistroUsuarioExcepcionConNullPassword() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario.prueba@gmail.com");

        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(usuario);
        });
    }

    @Test
    public void servicioRegistroUsuarioExcepcionConEmailRepetido() {
        addUsuarioBD();

        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("richard@umh.es");
        usuario.setPassword("12345678");

        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(usuario);
        });
    }

    @Test
    public void servicioRegistroUsuarioDevuelveUsuarioConId() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario.prueba@gmail.com");
        usuario.setPassword("12345678");

        UsuarioData usuarioNuevo = usuarioService.registrar(usuario);

        assertThat(usuarioNuevo.getId()).isNotNull();

        UsuarioData usuarioBD = usuarioService.findById(usuarioNuevo.getId());
        assertThat(usuarioBD).isEqualTo(usuarioNuevo);
    }

    @Test
    public void servicioConsultaUsuarioDevuelveUsuario() {
        Long usuarioId = addUsuarioBD();

        UsuarioData usuario = usuarioService.findByEmail("richard@umh.es");

        assertThat(usuario.getId()).isEqualTo(usuarioId);
        assertThat(usuario.getEmail()).isEqualTo("richard@umh.es");
        assertThat(usuario.getNombre()).isEqualTo("Richard Stallman");
    }

    @Test
    public void servicioListadoUsuariosDevuelveUsuariosRegistrados() {
        addUsuarioBD();

        UsuarioData usuario2 = new UsuarioData();
        usuario2.setEmail("ada@umh.es");
        usuario2.setNombre("Ada Lovelace");
        usuario2.setPassword("1234");
        usuarioService.registrar(usuario2);

        List<UsuarioData> usuarios = usuarioService.allUsuarios();

        assertThat(usuarios).hasSize(2);
        assertThat(usuarios)
                .extracting(UsuarioData::getEmail)
                .contains("richard@umh.es", "ada@umh.es");
    }

    @Test
    public void servicioConsultaUsuarioPorIdDevuelveUsuarioCorrecto() {
        Long usuarioId = addUsuarioBD();

        UsuarioData usuario = usuarioService.findById(usuarioId);

        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(usuarioId);
        assertThat(usuario.getEmail()).isEqualTo("richard@umh.es");
        assertThat(usuario.getNombre()).isEqualTo("Richard Stallman");
    }
}