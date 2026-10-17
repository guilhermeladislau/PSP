package dev.guiwtf.plataforma.alimentacao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.guiwtf.plataforma.TestcontainersConfiguration;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class AlimentoControllerTests {

	@Autowired
	MockMvc mvc;

	@Autowired
	AlimentoRepository repository;

	@BeforeEach
	void prepararDados() {
		repository.deleteAll();
		// Valores fictícios, só para teste.
		repository.save(alimento("Feijão carioca cozido"));
		repository.save(alimento("Arroz branco cozido"));
		repository.save(alimento("Arroz integral cozido"));
		repository.save(alimento("Suco 100% laranja"));
	}

	@Test
	void buscaIgnoraMaiusculasEAcentosEOrdenaPorNome() throws Exception {
		mvc.perform(get("/api/alimentos").param("busca", "ARROZ"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].nome").value("Arroz branco cozido"))
			.andExpect(jsonPath("$[1].nome").value("Arroz integral cozido"))
			.andExpect(jsonPath("$[0].caloriasKcal").value(100.0));

		mvc.perform(get("/api/alimentos").param("busca", "feijao"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].nome").value("Feijão carioca cozido"));
	}

	@Test
	void curingaDigitadoPeloUsuarioEhTratadoComoTexto() throws Exception {
		mvc.perform(get("/api/alimentos").param("busca", "%%"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(0));

		mvc.perform(get("/api/alimentos").param("busca", "100%"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void buscaInvalidaRetorna400() throws Exception {
		mvc.perform(get("/api/alimentos")).andExpect(status().isBadRequest());
		mvc.perform(get("/api/alimentos").param("busca", "a")).andExpect(status().isBadRequest());
		mvc.perform(get("/api/alimentos").param("busca", "x".repeat(101))).andExpect(status().isBadRequest());
	}

	private static Alimento alimento(String nome) {
		return new Alimento(nome, new BigDecimal("100"), new BigDecimal("100"), new BigDecimal("2.5"),
				new BigDecimal("20"), new BigDecimal("1"));
	}

}
