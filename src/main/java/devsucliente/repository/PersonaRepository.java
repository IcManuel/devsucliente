package devsucliente.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import devsucliente.models.Persona;

@Component
public interface PersonaRepository extends CrudRepository<Persona, Integer> {

	Optional<Persona> findByIdentificacion(String identificacion);
}
