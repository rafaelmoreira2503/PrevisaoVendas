package br.com.confiabilidade.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "temporeposicao")
public class TempoDeReposicao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private Integer tempoReposicaoFornecedor;
	private Integer tempoReposicaoRecebimento;
	private Integer tempoReposicaoRequisicao;
	private Integer tempoReposicaoTransporte;
	private Integer tempoReposicaoCompras;
	private Integer tempoReposicaoImportado;
	private Double tempoReposicao;

	@Id
	@GeneratedValue
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@NotNull
	public Integer getTempoReposicaoFornecedor() {
		return tempoReposicaoFornecedor;
	}
	@NotNull
	public Integer getTempoReposicaoRecebimento() {
		return tempoReposicaoRecebimento;
	}
	@NotNull
	public Integer getTempoReposicaoRequisicao() {
		return tempoReposicaoRequisicao;
	}
	@NotNull
	public Integer getTempoReposicaoTransporte() {
		return tempoReposicaoTransporte;
	}
	@NotNull
	public Integer getTempoReposicaoCompras() {
		return tempoReposicaoCompras;
	}

	public Integer getTempoReposicaoImportado() {
		return tempoReposicaoImportado;
	}

	public Double getTempoReposicao() {
		return tempoReposicao;
	}

	public void setTempoReposicaoFornecedor(Integer tempoReposicaoFornecedor) {
		this.tempoReposicaoFornecedor = tempoReposicaoFornecedor;
	}

	public void setTempoReposicaoRecebimento(Integer tempoReposicaoRecebimento) {
		this.tempoReposicaoRecebimento = tempoReposicaoRecebimento;
	}

	public void setTempoReposicaoRequisicao(Integer tempoReposicaoRequisicao) {
		this.tempoReposicaoRequisicao = tempoReposicaoRequisicao;
	}

	public void setTempoReposicaoTransporte(Integer tempoReposicaoTransporte) {
		this.tempoReposicaoTransporte = tempoReposicaoTransporte;
	}

	public void setTempoReposicaoCompras(Integer tempoReposicaoCompras) {
		this.tempoReposicaoCompras = tempoReposicaoCompras;
	}

	public void setTempoReposicaoImportado(Integer tempoReposicaoImportado) {

		this.tempoReposicaoImportado = tempoReposicaoImportado;
	}

	public void setTempoReposicao(Double tempoReposicao) {
		this.tempoReposicao = tempoReposicao;
	}

}
