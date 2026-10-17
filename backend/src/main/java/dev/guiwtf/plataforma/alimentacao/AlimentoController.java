package dev.guiwtf.plataforma.alimentacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

	static final int LIMITE_RESULTADOS = 20;

	private final AlimentoRepository repository;

	public AlimentoController(AlimentoRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	@Transactional(readOnly = true)
	public List<AlimentoResponse> buscar(@RequestParam @NotBlank @Size(min = 2, max = 100) String busca) {
		String padrao = "%" + escaparLike(busca.strip()) + "%";
		return repository.buscarPorNome(padrao, LIMITE_RESULTADOS).stream().map(AlimentoResponse::de).toList();
	}

	/** Faz '%', '_' e '\' digitados pelo usuário serem tratados como texto, não como curinga. */
	static String escaparLike(String texto) {
		return texto.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
	}

}
