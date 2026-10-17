package dev.guiwtf.plataforma.alimentacao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

	/**
	 * Busca por trecho do nome, sem diferenciar maiúsculas nem acentos.
	 *
	 * @param padrao padrão LIKE já escapado (com '\' como caractere de escape)
	 */
	@Query(value = """
			SELECT * FROM alimento
			WHERE unaccent(nome) ILIKE unaccent(:padrao) ESCAPE '\\'
			ORDER BY nome
			LIMIT :limite
			""", nativeQuery = true)
	List<Alimento> buscarPorNome(@Param("padrao") String padrao, @Param("limite") int limite);

}
