package br.com.confiabilidade.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

@Entity
@Table(name = "empresa")
@NamedQueries({
		@NamedQuery(name = Empresa.TODAS, query = "SELECT e FROM Empresa e order by e.nome"),
		@NamedQuery(name = Empresa.TOTAL, query = "SELECT COUNT(e) FROM Empresa e"),
		@NamedQuery(name = Empresa.EMPRESASPARAIMPORTACAOCRITICIDADE, query = "select e from Empresa e "),
		@NamedQuery(name = Empresa.EMPRESASPARAIMPORTACAOABC, query = "select e from Empresa e "),
		@NamedQuery(name = Empresa.EMPRESASPARACRITICAR, query = "select e from Empresa e where not exists (select distinct(pe.codigo_empresa) from PerguntaEmpresa "
				+ "pe where pe.codigo_empresa=e and pe.itemCriticado is not null and pe.areaCriticada is not null)"),
		@NamedQuery(name = Empresa.PORCNPJ, query = "from Empresa where cnpj = :cnpj") })
public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String TODAS = "Empresa.populaEmpresa";
	public final static String TOTAL = "Empresa.countEmpresasTotal";
	public final static String EMPRESASPARAIMPORTACAOCRITICIDADE = "Empresa.paraImportacaoCriticidade";
	public final static String EMPRESASPARAIMPORTACAOABC = "Empresa.paraImportacaoAbc";

	public final static String EMPRESASPARACRITICAR = "Empresa.paraCriticar";
	public final static String PORCNPJ = "Empresa.porCNPJ";

	private Long codigo;
	private String cnpj;
	private Date dataFim;
	private Date dataIni;
	private String iscricaoEstadual;
	private String nome;
	private String nomeFantasia;
	private String segmento;
	private String sponsor;
	private Integer totalItems;
	private Boolean ativa;

	private Endereco endereco;

	@Id
	@GeneratedValue
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@CNPJ
	@Column(name = "cnpj", length = 18, nullable = false)
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "data_ini", nullable = false)
	public Date getDataIni() {
		return this.dataIni;
	}

	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}

	@Future
	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim", nullable = false)
	public Date getDataFim() {
		return this.dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	@NotBlank
	@Column(name = "iscricao_estadual", nullable = false, length = 14)
	public String getIscricaoEstadual() {
		return this.iscricaoEstadual;
	}

	public void setIscricaoEstadual(String iscricaoEstadual) {
		this.iscricaoEstadual = iscricaoEstadual;
	}

	@NotBlank
	// Não pode estar em Branco
	@Size(max = 50)
	// Tamanho máximo de 50 e mínimo de 10 caracteres
	@Column(name = "nome", nullable = false, length = 50)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome == null ? null : nome.toUpperCase().trim();
	}

	@NotBlank
	// Não pode estar em Branco
	@Size(max = 30)
	// Tamanho máximo de 50 e mínimo de 10 caracteres
	@Column(name = "nome_fantasia", length = 30, nullable = false)
	public String getNomeFantasia() {
		return this.nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia == null ? null : nomeFantasia
				.toUpperCase().trim();
	}

	@NotBlank
	// Não pode estar em Branco
	@Size(max = 15)
	// Tamanho máximo de 50 e mínimo de 10 caracteres
	@Column(name = "segmento", length = 15, nullable = false)
	public String getSegmento() {
		return this.segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento == null ? null : segmento.toUpperCase().trim();
	}

	@NotBlank
	// Não pode estar em Branco
	@Size(max = 40)
	// Tamanho máximo de 50 e mínimo de 10 caracteres
	@Column(name = "sponsor", length = 40, nullable = false)
	public String getSponsor() {
		return this.sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor == null ? null : sponsor.toUpperCase().trim();
	}

	@Embedded
	public Endereco getEndereco() {
		return this.endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@NotNull
	@Column(name = "totalItems", length = 6, nullable = false)
	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public Boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}

	@Transient
	public boolean isNovo() {
		return getCodigo() == null;
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
		Empresa other = (Empresa) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
}