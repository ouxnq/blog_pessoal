package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.TemaRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TemaControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private TemaRepository temaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@BeforeAll
	void start() {
		temaRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", " "));
	}
	
	@Test
	@DisplayName("Criar Um Tema")
	public void deveCriarUmTema() {

		HttpEntity<Tema> corpoRequisicao = new HttpEntity<Tema>(
				new Tema(0L, "Descrição tema teste"));

		ResponseEntity<Tema> corpoResposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot").exchange("/temas", HttpMethod.POST,
				corpoRequisicao, Tema.class);

		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
	}
	
	@Test
    @DisplayName("Atualizar um Tema")
    public void deveAtualizarUmTema() {
		
        HttpEntity<Tema> temaInicial = new HttpEntity<Tema>(
				new Tema(0L, "Descrição tema teste inicial"));
        
        ResponseEntity<Tema> respostaCriacao = testRestTemplate.withBasicAuth("root@root.com", "rootroot").exchange("/temas", HttpMethod.POST,
				temaInicial, Tema.class);

        assertEquals(HttpStatus.CREATED, respostaCriacao.getStatusCode());
        
        
        Tema temaUpdate = new Tema(respostaCriacao.getBody().getId(), "Descrição tema atualizado");
        HttpEntity<Tema> corpoRequisicao = new HttpEntity<>(temaUpdate);
        

        ResponseEntity<Tema> corpoResposta = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange("/temas", HttpMethod.PUT, corpoRequisicao, Tema.class);

        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
    }
}
