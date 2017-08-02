package br.com.previsao.model.vo;

public class ReceitaTotal {
	
	private String codigoProduto;
	
	private Double total;
	
	private Double qtdSolicitada;

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public Double getTotal() {
		return total;
	}

	public Double getQtdSolicitada() {
		return qtdSolicitada;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public void setQtdSolicitada(Double qtdSolicitada) {
		this.qtdSolicitada = qtdSolicitada;
	}
	
	

}
