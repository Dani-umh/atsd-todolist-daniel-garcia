package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import todolist.authentication.ManagerUserSession;
import todolist.dto.EquipoData;
import todolist.dto.UsuarioData;
import todolist.service.EquipoService;
import todolist.service.UsuarioService;

import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model) {
        Long idUsuario = managerUserSession.usuarioLogeado();

        UsuarioData usuario = usuarioService.findById(idUsuario);
        List<EquipoData> equipos = equipoService.findAllOrdenadoPorNombre();
        List<EquipoData> equiposUsuario = equipoService.equiposUsuario(idUsuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipos", equipos);
        model.addAttribute("equiposUsuario", equiposUsuario);

        return "listaEquipos";
    }

    @GetMapping("/equipos/{id}")
    public String usuariosEquipo(@PathVariable(value = "id") Long idEquipo, Model model) {
        Long idUsuario = managerUserSession.usuarioLogeado();

        UsuarioData usuario = usuarioService.findById(idUsuario);
        EquipoData equipo = equipoService.recuperarEquipo(idEquipo);
        List<UsuarioData> usuarios = equipoService.usuariosEquipo(idEquipo);

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("usuarios", usuarios);

        return "usuariosEquipo";
    }

    @PostMapping("/equipos/{id}/unirse")
    public String unirseEquipo(@PathVariable(value = "id") Long idEquipo) {
        Long idUsuario = managerUserSession.usuarioLogeado();
        equipoService.añadirUsuarioAEquipo(idEquipo, idUsuario);
        return "redirect:/equipos";
    }

    @PostMapping("/equipos/{id}/salir")
    public String salirEquipo(@PathVariable(value = "id") Long idEquipo) {
        Long idUsuario = managerUserSession.usuarioLogeado();
        equipoService.eliminarUsuarioDeEquipo(idEquipo, idUsuario);
        return "redirect:/equipos";
    }
}