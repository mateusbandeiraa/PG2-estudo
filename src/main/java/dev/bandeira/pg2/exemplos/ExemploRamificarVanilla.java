package dev.bandeira.pg2.exemplos;

import java.util.List;
import java.util.stream.Collectors;

import dev.bandeira.pg2.util.Filme;
import dev.bandeira.pg2.util.Personagem;
import dev.bandeira.pg2.util.ResultadosBusca;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import kong.unirest.jackson.JacksonObjectMapper;

public class ExemploRamificarVanilla {
	private static final String URI_BASE = "https://swapi.dev/api";

	private static UnirestInstance http;

	static {
		http = Unirest.spawnInstance();
		http.config().defaultBaseUrl(URI_BASE);
		http.config().setObjectMapper(new JacksonObjectMapper());
	}

	public static void main(String[] args) {
		/*
		 * Utilize a API CompletableFuture para realizar as seguintes tarefas:
		 * 
		 * 
		 * 1. Obter uma lista de personagens (utilizando o método getAllPeople)
		 * 
		 * 2. Para cada personagem, obter e armazenar as informações de seus filmes
		 * (utilizando o método getFilmById). Dica: utilize personagem.comFilms(filmes)
		 * para criar uma nova instância de Personagem com os filmes obtidos.
		 * 
		 * 3. Imprimir a tabela de personagens/filmes (utilizando o método resultados)
		 */
	}

	private static ResultadosBusca<Personagem> getAllPeople() {
		var httpResponse = http.get("/people").asObject(new GenericType<ResultadosBusca<Personagem>>() {
		});
		if (httpResponse.getParsingError().isPresent()) {
			throw httpResponse.getParsingError().get();
		}
		return httpResponse.getBody();
	}

	private static Filme getFilmById(Integer idFilme) {
		var httpResponse = http.get("/films/{id}").routeParam("id", String.valueOf(idFilme)).asObject(Filme.class);
		if (httpResponse.getParsingError().isPresent()) {
			throw httpResponse.getParsingError().get();
		}
		return httpResponse.getBody();
	}

	private static void imprimir(List<Personagem> resultados) {

		var formato = "| %25s | %-120s |\n";

		System.out.printf(formato, "Nome", "Filmes");

		for (Personagem personagem : resultados) {
			var filmes = personagem.films().stream().map(Filme::title).collect(Collectors.joining("; "));
			System.out.printf(formato, personagem.name(), filmes);
		}
	}
}
