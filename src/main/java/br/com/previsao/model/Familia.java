package br.com.previsao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "familia")
@NamedQueries({ @NamedQuery(name = Familia.ALL, query = "Select a FROM Familia a"),
		@NamedQuery(name = Familia.COUNT, query = "select count(a) from Familia a"),
		@NamedQuery(name = Familia.DESCRICAO, query = "from Familia where upper(descricao) = :descricao") })
public class Familia implements Serializable {

	private static final long serialVersionUID = 1L;
	public final static String ALL = "Familia.populaFamilia";
	public final static String COUNT = "Familia.countFamiliaTotal";
	public final static String DESCRICAO = "Familia.descricao";

	private Long codigo;

	private Integer Version;

	private String descricao;

	private Empresa empresa;

	@Version
	public Integer getVersion() {
		return Version;
	}

	public void setVersion(Integer version) {
		Version = version;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao == null ? null : descricao.toUpperCase().trim();

	}

	@NotBlank
	@Column(nullable = false, length = 100)
	public String getDescricao() {
		return descricao;
	}

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
		Familia other = (Familia) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}