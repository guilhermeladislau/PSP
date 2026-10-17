package dev.guiwtf.plataforma.alimentacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/** Alimento do catálogo. Calorias e macros são por 100 g. */
@Entity
@Table(name = "alimento")
public class Alimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String nome;

	@Column(name = "porcao_padrao_gramas", nullable = false)
	private BigDecimal porcaoPadraoGramas;

	@Column(name = "calorias_kcal", nullable = false)
	private BigDecimal caloriasKcal;

	@Column(name = "proteinas_g", nullable = false)
	private BigDecimal proteinasG;

	@Column(name = "carboidratos_g", nullable = false)
	private BigDecimal carboidratosG;

	@Column(name = "gorduras_g", nullable = false)
	private BigDecimal gordurasG;

	/** Exigido pelo JPA. */
	protected Alimento() {
	}

	public Alimento(String nome, BigDecimal porcaoPadraoGramas, BigDecimal caloriasKcal, BigDecimal proteinasG,
			BigDecimal carboidratosG, BigDecimal gordurasG) {
		this.nome = nome;
		this.porcaoPadraoGramas = porcaoPadraoGramas;
		this.caloriasKcal = caloriasKcal;
		this.proteinasG = proteinasG;
		this.carboidratosG = carboidratosG;
		this.gordurasG = gordurasG;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public BigDecimal getPorcaoPadraoGramas() {
		return porcaoPadraoGramas;
	}

	public BigDecimal getCaloriasKcal() {
		return caloriasKcal;
	}

	public BigDecimal getProteinasG() {
		return proteinasG;
	}

	public BigDecimal getCarboidratosG() {
		return carboidratosG;
	}

	public BigDecimal getGordurasG() {
		return gordurasG;
	}

}
