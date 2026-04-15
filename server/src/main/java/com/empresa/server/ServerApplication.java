package com.empresa.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot del sistema hospitalario.
 * 
 * Esta clase es el punto de entrada de la aplicación. Define la configuración
 * base de Spring Boot y escanea automáticamente los componentes, servicios,
 * controladores y configuraciones en el paquete com.empresa.server y sus subpaquetes.
 * 
 * La anotación @SpringBootApplication combina:
 * - @Configuration: Define la clase como fuente de configuración
 * - @ComponentScan: Escanea los componentes de Spring
 * - @EnableAutoConfiguration: Activa la configuración automática de Spring Boot
 * 
 * Para ejecutar la aplicación:
 * <pre>
 * ServerApplication.main(new String[]{});
 * </pre>
 * 
 * La aplicación se ejecutará en el puerto especificado en application.properties
 * (por defecto puerto 8080).
 * 
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class ServerApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 * 
	 * @param args Argumentos de línea de comandos (opcionales)
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
