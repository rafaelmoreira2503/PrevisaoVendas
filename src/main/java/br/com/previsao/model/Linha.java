package br.com.previsao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "linha")
@NamedQueries({ @NamedQuery(name = Linha.ALL, query = "Select a FROM Linha a"),
		@NamedQuery(name = Linha.COUNT, query = "select count(a) from Linha a"),
		@NamedQuery(name = Linha.DESCRICAO, query = "from Linha where upper(descricao) = :descricao") })
public class Linha implements Serializable {

	private static final long serialVersionUID = 1L;
	public final static String ALL = "Linha.populaFamilia";
	public final static String COUNT = "Linha.countFamiliaTotal";
	public final static String DESCRICAO = "Linha.descricao";

	private Long codigo;

	private Integer Version;

	private String descricao;

	private Familia familia;

	private Usuario gerenteFilial;

	private List<Produto> produtos = new ArrayList<>();

	@OneToMany(mappedBy = "linha", fetch = FetchType.LAZY)
	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

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
	@JoinColumn(name = "id_familia")
	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	@ManyToOne
	@JoinColumn(name = "id_gerenteFilial")
	public Usuario getGerenteFilial() {
		return gerenteFilial;
	}

	public void setGerenteFilial(Usuario gerenteFilial) {
		this.gerenteFilial = gerenteFilial;
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
		Linha other = (Linha) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}