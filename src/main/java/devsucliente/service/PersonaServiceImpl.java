package devsucliente.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import devsucliente.models.Persona;
import devsucliente.repository.PersonaRepository;

@Repository
public class PersonaServiceImpl implements PersonaService {

	@Autowired
	PersonaRepository personaRepository;

	@Override
	public Persona findByIdentificacion(String identificacion) {
		return personaRepository.findByIdentificacion(identificacion).orElse(null);
	}

	@Override
	public Persona insert(Persona persona) {
		return personaRepository.save(persona);

	}

	@Override
	public Boolean update(Persona persona) {
		try {
			personaRepository.save(persona);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean delete(Persona persona) {
		try {
			personaRepository.delete(persona);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
