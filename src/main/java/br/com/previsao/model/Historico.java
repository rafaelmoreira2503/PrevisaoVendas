package br.com.previsao.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "historico")
@NamedQueries({ @NamedQuery(name = Historico.ALL, query = "Select h FROM Historico h"),
		@NamedQuery(name = Historico.COUNT, query = "select count(h) from Historico h"),
		@NamedQuery(name = Historico.PREVISAO, query = "select (h) from Historico h where h.mesesHistoricos = (select max(h2.mesesHistoricos) from Historico h2 where h2.produto = h.produto and h.produto.gerenteFilial.codigo=:codigo)"),
		@NamedQuery(name = Historico.COUNTPREVISAO, query = "select count(h) from Historico h where h.mesesHistoricos = (select max(h2.mesesHistoricos) from Historico h2 where h2.produto = h.produto and h.produto.gerenteFilial.codigo=:codigo)") })
public class Historico implements Serializable {

	private static final long serialVersionUID = 1L;
	public final static String ALL = "Historico.populaHistorico";
	public final static String COUNT = "Historico.countHistoricoTotal";
	public final static String DESCRICAO = "Historico.descricao";
	public final static String PREVISAO = "Historico.previsao";
	public final static String COUNTPREVISAO = "Historico.countprevisao";
	private Long codigo;

	private Integer Version;

	private Date mesesHistoricos;

	private Integer quantidade;

	private Produto produto;

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

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "meses_historicos", nullable = false)
	public Date getMesesHistoricos() {
		return mesesHistoricos;
	}

	public void setMesesHistoricos(Date mesesHistoricos) {
		this.mesesHistoricos = mesesHistoricos;
	}

	@Column(nullable = false, length = 3)
	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@ManyToOne
	@JoinColumn(name = "id_produto", nullable = false)
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto2) {
		this.produto = produto2;
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
		Historico other = (Historico) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}