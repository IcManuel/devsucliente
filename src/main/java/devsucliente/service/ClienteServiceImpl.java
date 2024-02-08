package devsucliente.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import devsucliente.models.Cliente;
import devsucliente.repository.ClienteRepository;

@Repository
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	ClienteRepository clienteRepository;

	@Override
	public Cliente findById(Integer id) {
		return clienteRepository.findById(id).orElse(null);
	}

	@Override
	public Cliente insert(Cliente cliente) {
		try {
			return clienteRepository.save(cliente);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean update(Cliente cliente) {
		try {
			clienteRepository.save(cliente);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean delete(Cliente cliente) {
		try {
			clienteRepository.delete(cliente);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Cliente> getAll() {
		return clienteRepository.obtenerTodos();
	}

	@Override
	public Cliente findByIdentificacion(String identificacion) {
		return clienteRepository.buscarPorIdentificacion(identificacion);
	}

}
