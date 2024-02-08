package devsucliente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "devsucliente.controller", "devsucliente.repository", "devsucliente.service",
		"devsucliente.config" })
@EntityScan(basePackages = { "devsucliente.models" })
@SpringBootApplication
public class DevsuClienteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevsuClienteApplication.class, args);
	}

}
