package devsucliente.service;

import java.util.List;

import devsucliente.models.Cliente;

public interface ClienteService {

	Cliente findById(Integer id);

	Cliente findByIdentificacion(String identificacion);

	Cliente insert(Cliente cliente);

	Boolean update(Cliente cliente);

	Boolean delete(Cliente cliente);

	List<Cliente> getAll();

}
