package br.com.confiabilidade.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "especialidade")
@NamedQueries({
		@NamedQuery(name = Especialidade.ALL, query = "SELECT e FROM Especialidade e"),
		@NamedQuery(name = Especialidade.COUNT, query = "SELECT COUNT(e) FROM Especialidade e"),
		@NamedQuery(name = Especialidade.DESCRICAO, query = "from Especialidade where upper(descricao) = :descricao") })
public class Especialidade implements Serializable {

	private static final long serialVersionUID = 1L;
	public final static String ALL = "Especialidade.all";

	public final static String COUNT = "Especialidade.count";
	public final static String DESCRICAO = "Especialidade.descricao";

	private Long codigo;

	private String descricao;

	private Integer Version;

	@Version
	public Integer getVersion() {
		return Version;
	}

	public void setVersion(Integer version) {
		Version = version;
	}

	@Id
	@GeneratedValue
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao == null ? null : descricao.toUpperCase()
				.trim();
	}

	@NotBlank
	@Column(nullable = false, length = 30)
	public String getDescricao() {
		return descricao;
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
		Especialidade other = (Especialidade) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}