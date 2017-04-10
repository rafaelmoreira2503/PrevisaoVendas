package br.com.confiabilidade.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "item")
@NamedQueries({
		@NamedQuery(name = Item.ITEMEXISTENTE, query = "select i from Item i where i.codigo=:codigo and i.areaItem.codigo =:area"),
		@NamedQuery(name = Item.TECNICOEXISTENTE, query = "select i from Item i where i.codigo=:codigo and i.areaItem.codigo =:area and i.tecnico.codigo=:tecnico"),
		@NamedQuery(name = Item.TOTALGERENTE, query = "select count(i) from Item i where i.empresa.codigo=:codigo"),
		@NamedQuery(name = Item.FEITOPARAGERENTE, query = "select count(i) from Item i where i.empresa.codigo=:codigo and i.status<>'PENDENTE'"),
		@NamedQuery(name = Item.TOTALENCARREGADO, query = "select count(i) from Item i where i.areaItem.codigo=:codigoArea and i.empresa.codigo=:codigoEmpresa"),
		@NamedQuery(name = Item.FETOPARAENCARREGADO, query = "select count(i) from Item i where i.areaItem.codigo=:codigoArea and "
				+ "i.empresa.codigo=:codigoEmpresa and i.status!='PENDENTE'"),
		@NamedQuery(name = Item.FETOPARATECNICO, query = "select count(distinct i)from Item i   "
				+ "where (i.status ='CRITICADO' or i.status ='JUSTIFICADO') and  i.tecnico.codigo=:codigo"),
		@NamedQuery(name = Item.TOTALTECNICO, query = "select count(distinct i) from Item i where i.tecnico.codigo=:codigo"),
		@NamedQuery(name = Item.ITEMDAEMPRESA, query = "select i from Item i where i.empresa=:empresa group by i.codigoItem") })
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String ITEMEXISTENTE = "Item.existente";
	public final static String TECNICOEXISTENTE = "Item.countItemsTotal";
	public final static String TOTALGERENTE = "Item.totalGerente";
	public final static String FEITOPARAGERENTE = "Item.feitoGerente";
	public final static String TOTALENCARREGADO = "Item.totalEncarregado";
	public final static String FETOPARAENCARREGADO = "Item.feitoEncarregado";
	public final static String TOTALTECNICO = "Item.totalTecnico";
	public final static String FETOPARATECNICO = "Item.feitoTecnico";
	public final static String ITEMDAEMPRESA = "Item.itemDaEmpresa";
	public final static String TOTALITEMMAIORCRITICIDADE = "Item.TotalitemMaiorCriticidade";
	private Long codigo;

	private String codigoItem;
	private String criticidade;
	private Date dataItem;
	private String descricao;
	private Justificativa justificativa;
	private StatusItem status;

	private Boolean naoAplicavel;
	private Area areaItem;
	private Empresa empresa;
	private Usuario tecnico;

	private PerguntaEmpresa perguntaEmpresa;

	@Id
	@GeneratedValue
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Size(max = 20)
	// Tamanho máximo de 20
	@NotBlank
	// Não pode estar em Branco
	@Column(name = "codigo_item", length = 20, nullable = false)
	public String getCodigoItem() {
		return this.codigoItem;
	}

	public void setCodigoItem(String codigoItem) {
		this.codigoItem = codigoItem;
	}

	@Size(max = 1)
	// Tamanho máximo de 1 caracteres
	@Column(name = "criticidade", length = 1)
	public String getCriticidade() {
		return this.criticidade;
	}

	public void setCriticidade(String criticidade) {
		this.criticidade = criticidade;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_item")
	public Date getDataItem() {
		return this.dataItem;
	}

	public void setDataItem(Date dataItem) {
		this.dataItem = dataItem;
	}

	@NotBlank
	// Não pode estar em Branco
	@Column(name = "descricao", nullable = false, length = 500)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao == null ? null : descricao.toUpperCase()
				.trim();
	}

	@Column(name = "justificativa", length = 40)
	@Enumerated(EnumType.STRING)
	public Justificativa getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(Justificativa justificativa) {
		this.justificativa = justificativa;
	}

	@Column(name = "status", length = 40)
	@Enumerated(EnumType.STRING)
	public StatusItem getStatus() {
		return status;
	}

	public void setStatus(StatusItem status) {
		this.status = status;
	}

	public Boolean getNaoAplicavel() {
		return naoAplicavel;
	}

	public Boolean isNaoAplicavel() {
		return naoAplicavel;
	}

	public void setNaoAplicavel(Boolean naoAplicavel) {
		this.naoAplicavel = naoAplicavel;
	}

	// Chave estrangeira de empresa em Item bi-directional many-to-one
	// association to
	// Empresa tem varios Items
	@ManyToOne
	@JoinColumn(name = "codigo_empresa")
	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_tecnico")
	public Usuario getTecnico() {
		return this.tecnico;
	}

	public void setTecnico(Usuario tecnico) {
		this.tecnico = tecnico;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_area")
	public Area getAreaItem() {
		return areaItem;
	}

	public void setAreaItem(Area areaItem) {
		this.areaItem = areaItem;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_perguntaEmpresa")
	public PerguntaEmpresa getPerguntaEmpresa() {
		return perguntaEmpresa;
	}

	public void setPerguntaEmpresa(PerguntaEmpresa perguntaEmpresa) {
		this.perguntaEmpresa = perguntaEmpresa;
	}

	@Transient
	public boolean isNovo() {
		return getDescricao() == null;
	}

	@Transient
	public boolean isExistente() {
		return !isNovo();
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
		Item other = (Item) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Transient
	public boolean isPendente() {
		return StatusItem.PENDENTE.equals(this.getStatus());
	}

	@Transient
	public boolean isAssociado() {
		return StatusItem.ASSOCIADO.equals(this.getStatus());
	}

	@Transient
	public boolean isCriticado() {
		return StatusItem.CRITICADO.equals(this.getStatus());
	}

	@Transient
	public boolean isNaoSeAplica() {
		return StatusItem.NAOSEAPLICA.equals(this.getStatus());
	}

	@Transient
	public boolean isJustificado() {
		return StatusItem.JUSTIFICADO.equals(this.getStatus());
	}

	@Transient
	public boolean isOutraArea() {
		return Justificativa.OUTRAAREA.equals(this.getJustificativa());
	}

	@Transient
	public boolean isNaoJustificavel() {
		return this.isPendente() || this.isCriticado();
	}

	@Transient
	public boolean isLimpavel() {
		return this.isExistente() && !isAssociado();
	}

	@Transient
	public boolean isNaoLimpavel() {
		return this.isExistente() && isAssociado();
	}

}