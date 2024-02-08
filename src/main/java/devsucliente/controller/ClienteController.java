package devsucliente.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devsucliente.dto.request.ClienteRequest;
import devsucliente.dto.responses.ClienteResponse;
import devsucliente.models.Cliente;
import devsucliente.models.Persona;
import devsucliente.service.ClienteService;
import devsucliente.service.PersonaService;
import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	PersonaService personaService;
	@Autowired
	ClienteService clienteService;

	@PostMapping
	public ResponseEntity<Object> insertar(@Valid @RequestBody ClienteRequest clienteRequest,
			BindingResult bindingResult) {
		Map<String, Object> response = new HashMap<>();
		List<String> errors = new ArrayList<>();
		if (Objects.isNull(clienteRequest.getContrasena()) || clienteRequest.getContrasena().isBlank()) {
			errors.add("Requiere contraseña");
			response.put("errors", errors);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError e : bindingResult.getFieldErrors()) {
				errors.add(e.getDefaultMessage());
			}
			response.put("errors", errors);
		}
		if (!errors.isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		Persona persona = personaService.findByIdentificacion(clienteRequest.getIdentificacion());
		if (!Objects.isNull(persona)) {
			errors.add("Ya existe un cliente con esa identificación");
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			persona = new Persona();
		}
		if (!Objects.isNull(clienteRequest.getDireccion())) {
			persona.setDireccion(clienteRequest.getDireccion().toUpperCase());
		}
		persona.setEdad(clienteRequest.getEdad());
		if (!Objects.isNull(clienteRequest.getGenero())) {
			persona.setGenero(clienteRequest.getGenero().toUpperCase());
		}
		persona.setIdentificacion(clienteRequest.getIdentificacion());
		persona.setNombre(clienteRequest.getNombre().toUpperCase());
		persona.setTelefono(clienteRequest.getTelefono());
		try {
			persona = personaService.insert(persona);
		} catch (Exception e) {
			errors.add(e.getMessage());
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Cliente cliente = new Cliente();
		cliente.setPersona(persona);
		cliente.setContrasena(clienteRequest.getContrasena());
		cliente.setEstado(true);

		try {
			cliente = clienteService.insert(cliente);
		} catch (Exception e) {
			errors.add(e.getMessage());
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("cliente", cliente);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> modificar(@Valid @RequestBody ClienteRequest clienteRequest,
			BindingResult bindingResult, @PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		List<String> errors = new ArrayList<>();
		Cliente cliente = clienteService.findById(id);
		if (cliente == null) {
			errors.add("Cliente con id".concat(id.toString()).concat(" no existe"));
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError e : bindingResult.getFieldErrors()) {
				errors.add(e.getDefaultMessage());
			}
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		Persona persona = cliente.getPersona();
		Persona existePersona = personaService.findByIdentificacion(clienteRequest.getIdentificacion());
		if (!Objects.isNull(existePersona) && !existePersona.getId().equals(persona.getId())) {
			errors.add("Ya existe un cliente con la identificación ingresada: "
					.concat(clienteRequest.getIdentificacion()));
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		if (!Objects.isNull(clienteRequest.getDireccion())) {
			persona.setDireccion(clienteRequest.getDireccion().toUpperCase());
		}
		if (!Objects.isNull(clienteRequest.getEdad())) {
			persona.setEdad(clienteRequest.getEdad());
		}
		if (!Objects.isNull(clienteRequest.getGenero())) {
			persona.setGenero(clienteRequest.getGenero().toUpperCase());
		}
		persona.setIdentificacion(clienteRequest.getIdentificacion());
		persona.setNombre(clienteRequest.getNombre().toUpperCase());
		if (!Objects.isNull(clienteRequest.getTelefono())) {
			persona.setTelefono(clienteRequest.getTelefono());
		}
		if (!personaService.update(persona)) {
			errors.add("Error al actualizar persona");
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		cliente.setPersona(persona);
		if (!Objects.isNull(clienteRequest.getContrasena())) {
			cliente.setContrasena(clienteRequest.getContrasena());
		}
		if (!Objects.isNull(clienteRequest.getEstado())) {
			cliente.setEstado(clienteRequest.getEstado());
		}

		if (!clienteService.update(cliente)) {
			errors.add("Error al actualizar cliente");
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("cliente", cliente);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping
	public List<Cliente> obtener() {
		return clienteService.getAll();
	}

	@GetMapping("/{identificacion}")
	public ResponseEntity<Object> obtenerPorIdentificacion(@PathVariable String identificacion) {
		Cliente cliente = clienteService.findByIdentificacion(identificacion);
		if (cliente == null) {
			return new ResponseEntity<>("Cliente no existe", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(
				new ClienteResponse(cliente.getId(), identificacion, cliente.getPersona().getNombre(),
						cliente.getPersona().getGenero(), cliente.getPersona().getEdad(),
						cliente.getPersona().getDireccion(), cliente.getPersona().getTelefono(), cliente.getEstado()),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		List<String> errors = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();

		Cliente cliente = clienteService.findById(id);
		if (cliente == null) {
			errors.add("Cliente con id: ".concat(id.toString()).concat(" no existe"));
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		if (!clienteService.delete(cliente)) {
			errors.add("No se puede borrar el cliente posee cuentas");
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (!personaService.delete(cliente.getPersona())) {
			errors.add("No se puede borrar el cliente posee cuentas");
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("ok", true);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
