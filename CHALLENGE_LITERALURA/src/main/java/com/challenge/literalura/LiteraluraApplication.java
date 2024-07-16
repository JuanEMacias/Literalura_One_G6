package com.challenge.literalura;

import com.challenge.literalura.mainclass.Aplicación;
import com.challenge.literalura.models.Autor;
import com.challenge.literalura.models.Libro2;
import com.challenge.literalura.repository.AutorRepository;
import com.challenge.literalura.repository.LibroRepository;
import com.challenge.literalura.service.APIRequest;
import com.challenge.literalura.service.Conversion;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Aplicación menu = new Aplicación(libroRepository, autorRepository);
		menu.showMenu();
	}
}
