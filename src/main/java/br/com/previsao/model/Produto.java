package br.com.previsao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "produto")
@NamedQueries({ @NamedQuery(name = Produto.ALL, query = "Select a FROM Produto a"),
		@NamedQuery(name = Produto.COUNT, query = "select count(a) from Produto a"),
		@NamedQuery(name = Produto.DESCRICAO, query = "from Produto where upper(descricao) = :descricao") })
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;
	public final static String ALL = "produto.populaproduto";
	public final static String COUNT = "produto.countprodutoTotal";
	public final static String DESCRICAO = "produto.descricao";

	private Long codigo;

	private Integer Version;

	private String codigoProduto;
	
	private String codigoFilial;

	private String descricao;

	private BigDecimal valor;
	
	private Empresa empresa;

	private Linha linha;
	
	private Integer primeiraQuantidade;
	
	private Integer segundaQuantidade;
	
	private Integer terceiraQuantidade;
	
	private Integer ultimaQuantidade;

	private Usuario gerenteFilial;

	private Double fatorAmortecimentoExponencial;

	private Double quantidadeRecente;

	private BigDecimal quantidadeReais;
	
	private Date primeiroMesAnterior;
	
	private Date segundoMesAnterior;
	
	private Date terceiroMesAnterior;
	
	private Date anoAnterior;

	private List<Historico> historicos = new ArrayList<>();

	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
	}

	public Produto() {
	}

	public Produto(String codigoProduto, String descricao, BigDecimal valor) {
		this.codigoProduto = codigoProduto;
		this.descricao = descricao;
		this.valor = valor;
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

	@NotBlank
	@Column(nullable = false, length = 50)
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao == null ? null : descricao.toUpperCase().trim();

	}

	@NotBlank
	@Column(nullable = false, length = 100)
	public String getDescricao() {
		return descricao;
	}

	@Column(name = "valor", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "fatorAmortecimentoExponencial", nullable = true, precision = 10, scale = 2)
	public Double getFatorAmortecimentoExponencial() {
		return fatorAmortecimentoExponencial;
	}

	public void setFatorAmortecimentoExponencial(Double fatorAmortecimentoExponencial) {
		this.fatorAmortecimentoExponencial = fatorAmortecimentoExponencial;
	}

	@Column(name = "quantidade_recente")
	public Double getQuantidadeRecente() {
		return quantidadeRecente;
	}

	public void setQuantidadeRecente(Double quantidadeRecente) {
		this.quantidadeRecente = quantidadeRecente;
	}

	@Column(name = "quantidade_reais", nullable = true, precision = 10, scale = 2)
	public BigDecimal getQuantidadeReais() {
		return quantidadeReais;
	}

	public void setQuantidadeReais(BigDecimal quantidadeReais) {
		this.quantidadeReais = quantidadeReais;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_empresa")
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_linha")
	public Linha getLinha() {
		return linha;
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_filial")
	public Usuario getGerenteFilial() {
		return gerenteFilial;
	}

	public void setGerenteFilial(Usuario gerenteFilial) {
		this.gerenteFilial = gerenteFilial;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "primeiroMesAnterior", nullable = false)
	public Date getPrimeiroMesAnterior() {
		return primeiroMesAnterior;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "segundoMesAnterior", nullable = false)
	public Date getSegundoMesAnterior() {
		return segundoMesAnterior;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "terceiroMesAnterior", nullable = false)
	public Date getTerceiroMesAnterior() {
		return terceiroMesAnterior;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "anoAnterior", nullable = false)
	public Date getAnoAnterior() {
		return anoAnterior;
	}

	
	public void setPrimeiroMesAnterior(Date primeiroMesAnterior) {
		this.primeiroMesAnterior = primeiroMesAnterior;
	}

	public void setSegundoMesAnterior(Date segundoMesAnterior) {
		this.segundoMesAnterior = segundoMesAnterior;
	}

	public void setTerceiroMesAnterior(Date terceiroMesAnterior) {
		this.terceiroMesAnterior = terceiroMesAnterior;
	}

	public void setAnoAnterior(Date anoAnterior) {
		this.anoAnterior = anoAnterior;
	}
	@Column(nullable = false)
	public String getCodigoFilial() {
		return codigoFilial;
	}

	public void setCodigoFilial(String codigoFilial) {
		this.codigoFilial = codigoFilial;
	}
	
	
	@Column(nullable = false, length = 3)
	public Integer getPrimeiraQuantidade() {
		return primeiraQuantidade;
	}
	@Column(nullable = false, length = 3)
	public Integer getSegundaQuantidade() {
		return segundaQuantidade;
	}
	@Column(nullable = false, length = 3)
	public Integer getTerceiraQuantidade() {
		return terceiraQuantidade;
	}

	@Column(nullable = false, length = 3)
	public Integer getUltimaQuantidade() {
		return ultimaQuantidade;
	}

	public void setPrimeiraQuantidade(Integer primeiraQuantidade) {
		this.primeiraQuantidade = primeiraQuantidade;
	}

	public void setSegundaQuantidade(Integer segundaQuantidade) {
		this.segundaQuantidade = segundaQuantidade;
	}

	public void setTerceiraQuantidade(Integer terceiraQuantidade) {
		this.terceiraQuantidade = terceiraQuantidade;
	}

	public void setUltimaQuantidade(Integer ultimaQuantidade) {
		this.ultimaQuantidade = ultimaQuantidade;
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
		Produto other = (Produto) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	

}