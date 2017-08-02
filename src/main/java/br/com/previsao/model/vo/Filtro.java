package br.com.previsao.model.vo;

import java.math.BigDecimal;

public class Filtro {

	public Filtro() {
	}

	private String codigoProduto;

	private String descricao;

	private String Filial;

	private Double quantidade;

	private Number soma;

	private BigDecimal valor;

	private Number receita;

	public Number getSoma() {
		return soma;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Number getReceita() {
		return receita;
	}

	public void setSoma(Number soma) {
		this.soma = soma;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public void setReceita(Number receita) {
		this.receita = receita;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getFilial() {
		return Filial;
	}

	public void setFilial(String filial) {
		Filial = filial;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Number getTotal() {
		return soma;
	}

	public void setTotal(Number total) {
		this.soma = total;
	}

}
