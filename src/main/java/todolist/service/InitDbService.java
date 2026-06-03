package todolist.service;

import todolist.model.Equipo;
import todolist.model.Tarea;
import todolist.model.Usuario;
import todolist.repository.EquipoRepository;
import todolist.repository.TareaRepository;
import todolist.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("dev")
public class InitDbService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @PostConstruct
    public void initDatabase() {
        Usuario usuario = new Usuario("richard@umh.es");
        usuario.setNombre("Richard Stallman");
        usuario.setPassword("1234");
        usuarioRepository.save(usuario);

        Tarea tarea1 = new Tarea(usuario, "Create the GNU General Public License");
        tareaRepository.save(tarea1);

        Tarea tarea2 = new Tarea(usuario, "Buy milk, cereals and coffee");
        tareaRepository.save(tarea2);

        Equipo equipo1 = new Equipo("Project AAA", "Equipo de ejemplo para el proyecto AAA");
        equipo1.addUsuario(usuario);
        equipoRepository.save(equipo1);

        Equipo equipo2 = new Equipo("Project BBB", "Equipo de ejemplo para el proyecto BBB");
        equipo2.addUsuario(usuario);
        equipoRepository.save(equipo2);
    }
}