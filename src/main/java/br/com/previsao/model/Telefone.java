package br.com.previsao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotBlank;

@Embeddable
public class Telefone implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fixo;
	private String celular;
	private String ramal;

	// Tamanho máximo de 14 caracteres
	@Column(name = "tel_fixo", length = 14)
	public String getFixo() {
		return fixo;
	}

	public void setFixo(String fixo) {
		this.fixo = fixo;
	}

	// Não pode estar em Branco
	@NotBlank
	// Tamanho máximo de 15 caracteres
	@Column(name = "tel_celular", length = 15, nullable = false)
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	// Tamanho máximo de 4 caracteres
		@Column(name = "ramal", length = 4)
	public String getRamal() {
		return ramal;
	}

	
	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

}