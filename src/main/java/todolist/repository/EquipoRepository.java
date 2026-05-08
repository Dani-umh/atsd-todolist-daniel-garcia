package todolist.repository;

import org.springframework.data.repository.CrudRepository;
import todolist.model.Equipo;

import java.util.List;

public interface EquipoRepository extends CrudRepository<Equipo, Long> {

    List<Equipo> findAll();
}