package todolist.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todolist.dto.EquipoData;
import todolist.dto.UsuarioData;
import todolist.model.Equipo;
import todolist.model.Usuario;
import todolist.repository.EquipoRepository;
import todolist.repository.UsuarioRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public EquipoData crearEquipo(String nombre) {
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);
        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional(readOnly = true)
    public EquipoData recuperarEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);

        if (equipo == null) {
            throw new EquipoServiceException("Equipo no encontrado");
        }

        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional(readOnly = true)
    public List<EquipoData> findAllOrdenadoPorNombre() {
        return equipoRepository.findAll().stream()
                .sorted(Comparator.comparing(Equipo::getNombre))
                .map(equipo -> modelMapper.map(equipo, EquipoData.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void añadirUsuarioAEquipo(Long id, Long id1) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);

        if (equipo == null) {
            throw new EquipoServiceException("Equipo no encontrado");
        }

        Usuario usuario = usuarioRepository.findById(id1).orElse(null);

        if (usuario == null) {
            throw new EquipoServiceException("Usuario no encontrado");
        }

        equipo.addUsuario(usuario);
    }

    @Transactional
    public void eliminarUsuarioDeEquipo(Long id, Long id1) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);

        if (equipo == null) {
            throw new EquipoServiceException("Equipo no encontrado");
        }

        Usuario usuario = usuarioRepository.findById(id1).orElse(null);

        if (usuario == null) {
            throw new EquipoServiceException("Usuario no encontrado");
        }

        equipo.removeUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioData> usuariosEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);

        if (equipo == null) {
            throw new EquipoServiceException("Equipo no encontrado");
        }

        return equipo.getUsuarios().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioData.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipoData> equiposUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            throw new EquipoServiceException("Usuario no encontrado");
        }

        return usuario.getEquipos().stream()
                .map(equipo -> modelMapper.map(equipo, EquipoData.class))
                .collect(Collectors.toList());
    }
}