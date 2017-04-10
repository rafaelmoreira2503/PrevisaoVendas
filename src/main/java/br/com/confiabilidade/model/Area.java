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
@Table(name = "area")
@NamedQueries({
		@NamedQuery(name = Area.ALL, query = "Select a FROM Area a"),
		@NamedQuery(name = Area.COUNT, query = "select count(a) from Area a"),
		@NamedQuery(name = Area.DESCRICAO, query = "from Area where upper(descricao) = :descricao") })
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;
	public final static String ALL = "Area.populaArea1";
	public final static String COUNT = "Area.countAreaTotal";
	public final static String DESCRICAO = "Area.descricao";

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

	private List<Usuario> encarregados = new ArrayList<>();

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
	@Column(nullable = false, length = 50)
	public String getDescricao() {
		return descricao;
	}

	@OneToMany(mappedBy = "areaEncarregado", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	public List<Usuario> getEncarregados() {
		return encarregados;
	}

	public void setEncarregados(List<Usuario> encarregados) {
		this.encarregados = encarregados;
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
		Area other = (Area) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}