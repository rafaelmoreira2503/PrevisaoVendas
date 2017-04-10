package br.com.confiabilidade.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "pergunta")
@NamedQueries({
		@NamedQuery(name = Pergunta.TODAS, query = "Select p from Pergunta p"),
		@NamedQuery(name = Pergunta.TOTAL, query = "Select count(p) from Pergunta p "),
		@NamedQuery(name = Pergunta.ASSOCIADAS, query = "select distinct(pe.codigo_pergunta) from Pergunta p join p.perguntaEmpresas pe where pe.codigo_empresa.codigo=:codigo"),
		@NamedQuery(name = Pergunta.NAOASSOCIADAS, query = "select p from Pergunta p where not exists( "
				+ "from PerguntaEmpresa pe where pe.codigo_empresa.codigo=:codigo and pe.codigo_pergunta= p.codigo)") })
public class Pergunta implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String TODAS = "Pergunta.todas";

	public final static String TOTAL = "Pergunta.total";

	public final static String ASSOCIADAS = "Perguntas.associadas";

	public final static String NAOASSOCIADAS = "Perguntas.naoAassociadas";

	private Long codigo;
	private Integer Version;

	private String descricao;

	@Version
	public Integer getVersion() {
		return Version;
	}

	public void setVersion(Integer version) {
		Version = version;
	}

	private List<PerguntaEmpresa> perguntaEmpresas = new ArrayList<>();

	@Id
	@GeneratedValue
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@NotBlank
	@Column(nullable = false, length = 60)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao == null ? null : descricao.toUpperCase()
				.trim();
	}

	@OneToMany(mappedBy = "codigo_pergunta", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	public List<PerguntaEmpresa> getPerguntaEmpresas() {
		return perguntaEmpresas;
	}

	public void setPerguntaEmpresas(List<PerguntaEmpresa> perguntaEmpresas) {
		this.perguntaEmpresas = perguntaEmpresas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pergunta other = (Pergunta) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}