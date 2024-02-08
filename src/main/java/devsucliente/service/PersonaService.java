package devsucliente.service;

import devsucliente.models.Persona;

public interface PersonaService {

	Persona findByIdentificacion(String identificacion);

	Persona insert(Persona persona);

	Boolean update(Persona persona);

	Boolean delete(Persona persona);
}
