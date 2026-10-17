package dev.guiwtf.plataforma.alimentacao;

import java.math.BigDecimal;

/** Representação pública de um alimento. Calorias e macros por 100 g. */
public record AlimentoResponse(Long id, String nome, BigDecimal porcaoPadraoGramas, BigDecimal caloriasKcal,
		BigDecimal proteinasG, BigDecimal carboidratosG, BigDecimal gordurasG) {

	static AlimentoResponse de(Alimento a) {
		return new AlimentoResponse(a.getId(), a.getNome(), a.getPorcaoPadraoGramas(), a.getCaloriasKcal(),
				a.getProteinasG(), a.getCarboidratosG(), a.getGordurasG());
	}

}
