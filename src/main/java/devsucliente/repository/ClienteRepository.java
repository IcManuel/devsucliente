package devsucliente.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import devsucliente.models.Cliente;

@Component
public interface ClienteRepository extends CrudRepository<Cliente, Integer> {

	@Query("select c from Cliente c join c.persona p where p.identificacion =:identificacion")
	Cliente buscarPorIdentificacion(String identificacion);

	@Query("select c from Cliente c order by c.id")
	List<Cliente> obtenerTodos();
}
