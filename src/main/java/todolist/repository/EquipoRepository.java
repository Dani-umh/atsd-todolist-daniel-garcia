package todolist.repository;

import org.springframework.data.repository.CrudRepository;
import todolist.model.Equipo;

public interface EquipoRepository extends CrudRepository<Equipo, Long> {
}