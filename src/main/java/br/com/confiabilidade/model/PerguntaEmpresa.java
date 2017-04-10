package br.com.confiabilidade.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pergunta_empresa")
@NamedQueries({

		@NamedQuery(name = PerguntaEmpresa.TODAS, query = "select distinct(pe) from PerguntaEmpresa pe where pe.itemCriticado is null and pe.codigo_empresa.codigo=:codigo"),
		@NamedQuery(name = PerguntaEmpresa.PERGUNTACOMITEM, query = " select distinct(pe) from PerguntaEmpresa pe where"
				+ " pe.codigo_empresa.codigo=:codigo and pe.codigo_pergunta.codigo=:codigoPergunta "
				+ "and pe.itemCriticado.codigo=:codigoItem and pe.areaCriticada.codigo=:codigoArea "),
		@NamedQuery(name = PerguntaEmpresa.PERGUNTAEXISTENTE, query = " select distinct(pe) from PerguntaEmpresa pe where pe.codigo_empresa=:empresa"
				+ " and pe.codigo_pergunta=:pergunta"
				+ " and pe.itemCriticado.codigo is null"
				+ " and pe.areaCriticada.codigo is null ") })
public class PerguntaEmpresa implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String PERGUNTACOMITEM = "perguntaEmpresa.perguntaComItem";
	public final static String PERGUNTAEXISTENTE = "perguntEempresa.perguntaExistente";
	public final static String TODAS = "perguntaEmpresa.todas";

	private Long codigo;

	private Pergunta codigo_pergunta;

	private Empresa codigo_empresa;

	private Item itemCriticado;

	private Resposta resposta;

	private Area areaCriticada;

	@Id
	@GeneratedValue
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_pergunta")
	public Pergunta getCodigo_pergunta() {
		return codigo_pergunta;
	}

	public void setCodigo_pergunta(Pergunta codigo_pergunta) {
		this.codigo_pergunta = codigo_pergunta;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_empresa")
	public Empresa getCodigo_empresa() {
		return codigo_empresa;
	}

	public void setCodigo_empresa(Empresa codigo_empresa) {
		this.codigo_empresa = codigo_empresa;
	}

	@Column(name = "resposta", length = 40)
	@Enumerated(EnumType.STRING)
	public Resposta getResposta() {
		return resposta;
	}

	public void setResposta(Resposta resposta) {
		this.resposta = resposta;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_item")
	public Item getItemCriticado() {
		return itemCriticado;
	}

	public void setItemCriticado(Item itemCriticado) {
		this.itemCriticado = itemCriticado;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_area")
	public Area getAreaCriticada() {
		return areaCriticada;
	}

	public void setAreaCriticada(Area areaCriticada) {
		this.areaCriticada = areaCriticada;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PerguntaEmpresa other = (PerguntaEmpresa) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

}
