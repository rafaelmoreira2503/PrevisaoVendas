package br.com.confiabilidade.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "itemabc")
@NamedQueries({ @NamedQuery(name = ItemAbc.ITEMABCORDENADO, query = "select i from ItemAbc i where i.empresa=:empresa order by i.item , i.abc")

})
public class ItemAbc implements Comparable<ItemAbc> {
	public final static String ITEMABCORDENADO = "ItemAbc.ordenado";

	public final static String VERIFICAITEMSABCXCRITICADOS = "ItemAbc.verificaItemsAbc";

	private Long codigo;

	private String codigoItemAbc;
	private String abc;
	private Double media;
	private Double demanda;

	private Double percentual;
	private Integer percentagem;

	private Date dataItemAbc;
	private String descricaoAbc;

	private Long quantidadeRepor;
	private Long estoqueDeSeguranca;
	private Long pontoDeReposicao;
	private Long estoqueMaximo;

	private Empresa empresa;

	private Item item;

	private TempoDeReposicao tempoDeReposicao;

	@Id
	@GeneratedValue
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Size(max = 20)
	@NotBlank
	@Column(name = "codigo_Abc", length = 20, nullable = false)
	public String getCodigoItemAbc() {
		return codigoItemAbc;
	}

	public void setCodigoItemAbc(String codigoItemAbc) {
		this.codigoItemAbc = codigoItemAbc;
	}

	@Column(length = 1)
	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}

	public Double getMedia() {
		return media;
	}

	public void setMedia(Double media) {
		this.media = media;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_Abc")
	public Date getDataItemAbc() {
		return dataItemAbc;
	}

	public void setDataItemAbc(Date dataItemAbc) {
		this.dataItemAbc = dataItemAbc;
	}

	public String getDescricaoAbc() {
		return descricaoAbc;
	}

	public void setDescricaoAbc(String descricaoAbc) {
		this.descricaoAbc = descricaoAbc;
	}

	public Double getPercentual() {
		return percentual;
	}

	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}

	public Integer getPercentagem() {
		return percentagem;
	}

	public void setPercentagem(Integer percentagem) {
		this.percentagem = percentagem;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_empresaAbc")
	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@OneToOne
	@JoinColumn(name = "codigo_itemNoAbc")
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Double getDemanda() {
		return demanda;
	}

	public void setDemanda(Double demanda) {
		this.demanda = demanda;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "codigo_tempo_reposicao")
	public TempoDeReposicao getTempoDeReposicao() {
		return tempoDeReposicao;
	}

	public void setTempoDeReposicao(TempoDeReposicao tempoDeReposicao) {
		this.tempoDeReposicao = tempoDeReposicao;
	}

	public Long getQuantidadeRepor() {
		return quantidadeRepor;
	}

	public Long getPontoDeReposicao() {
		return pontoDeReposicao;
	}

	public void setPontoDeReposicao(Long pontoDeReposicao) {
		this.pontoDeReposicao = pontoDeReposicao;
	}

	public Long getEstoqueMaximo() {
		return estoqueMaximo;
	}

	public void setEstoqueMaximo(Long estoqueMaximo) {
		this.estoqueMaximo = estoqueMaximo;
	}

	public Long getEstoqueDeSeguranca() {
		return estoqueDeSeguranca;
	}

	public void setEstoqueDeSeguranca(Long estoqueDeSeguranca) {
		this.estoqueDeSeguranca = estoqueDeSeguranca;
	}

	public void setQuantidadeRepor(Long quantidadeRepor) {
		this.quantidadeRepor = quantidadeRepor;
	}

	@Override
	public int compareTo(ItemAbc o) {
		if (o.getMedia() < this.getMedia()) {
			return -1;
		} else if (o.getMedia() > this.getMedia()) {
			return 1;
		}

		return 0;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ItemAbc other = (ItemAbc) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		return true;
	}

}
