package br.com.confiabilidade.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.com.confiabilidade.validator.CEP;
import br.com.confiabilidade.validator.UF;

@Embeddable
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;

	@Size(max = 50)
	@Column(name = "logradouro", length = 50)
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro == null ? null : logradouro.toUpperCase()
				.trim();
	}

	@Size(max = 5)
	@Column(name = "numero", length = 5)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Size(max = 30)
	@Column(name = "complemento", length = 30)
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento == null ? null : complemento
				.toUpperCase().trim();
	}

	@Size(max = 30)
	@Column(name = "bairro", length = 30)
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro == null ? null : bairro.toUpperCase().trim();
	}

	@Size(max = 30)
	@Column(name = "cidade", length = 30)
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade==null?null:cidade.toUpperCase().trim();
	}

	@UF
	@Size(max = 2)
	@Column(name = "uf", length = 2)
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf == null ? null : uf.toUpperCase().trim();
	}

	@CEP
	@NotBlank
	@Size(max = 10)
	@Column(name = "cep", length = 10)
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

}