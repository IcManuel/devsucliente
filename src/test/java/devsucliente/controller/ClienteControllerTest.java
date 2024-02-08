package devsucliente.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import devsucliente.dto.request.ClienteRequest;
import devsucliente.models.Cliente;
import devsucliente.models.Persona;
import devsucliente.repository.ClienteRepository;
import devsucliente.service.ClienteServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private ClienteRepository clienteRepositoryMock;

	@InjectMocks
	private ClienteServiceImpl clienteService;

	// PRUEBA UNITARIA
	@Test
	public void testGuardarCliente() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			int indiceAleatorio = random.nextInt(caracteres.length());
			sb.append(caracteres.charAt(indiceAleatorio));
		}
		Persona persona = new Persona();
		persona.setDireccion("DIRECCION TEST");
		persona.setEdad(10);
		persona.setGenero("M");
		persona.setIdentificacion(sb.toString());
		persona.setNombre("TEST UNITARIO");
		persona.setTelefono("TELEFONO");
		Cliente cliente = new Cliente();
		cliente.setPersona(persona);
		cliente.setContrasena("contrasena");
		cliente.setEstado(true);
		when(clienteRepositoryMock.save(cliente)).thenReturn(cliente);
		clienteService.insert(cliente);
		verify(clienteRepositoryMock).save(cliente);
	}

	// PRUEBAS DE INTEGRACION
	@Test
	public void insertarValidandoQueYaExistaIdentificacino() throws Exception {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			int indiceAleatorio = random.nextInt(caracteres.length());
			sb.append(caracteres.charAt(indiceAleatorio));
		}
		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setContrasena("TEST");
		clienteRequest.setDireccion("TEST");
		clienteRequest.setEdad(70);
		clienteRequest.setEstado(true);
		clienteRequest.setGenero("TEST");
		clienteRequest.setIdentificacion(sb.toString());
		clienteRequest.setNombre("TEST");
		clienteRequest.setTelefono("TEST");
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = objectMapper.writeValueAsString(clienteRequest);

//		mockMvc.perform(get("/clientes")).andExpect(status().isOk());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/clientes").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(result -> {
					if (result.getResponse().getStatus() == HttpStatus.OK.value()) {
					} else if (result.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value()) {
						String responseContent = result.getResponse().getContentAsString();
						if (responseContent.contains("Ya existe"))
							assertTrue(true);
						else
							fail("Respuesta inesperada: " + result.getResponse().getContentAsString());
					} else {
						fail("Respuesta inesperada: " + result.getResponse().getStatus());
					}
				});
	}

	@Test
	public void obtener() throws Exception {
		mockMvc.perform(get("/clientes")).andExpect(status().isOk());
	}
}
