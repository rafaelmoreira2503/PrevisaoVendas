package br.com.confiabilidade.controller;

public class ItemAbcde implements Comparable<ItemAbcde> {

	private String codigoItemAbc;

	private String descricao;

	private Double media;

	private Integer percentagem;
	
	private Integer Demanda ;

	private Double percentual;

	private String Abc;

	public String getCodigoItemAbc() {
		return codigoItemAbc;
	}

	public void setCodigoItemAbc(String codigoItemAbc) {
		this.codigoItemAbc = codigoItemAbc;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getMedia() {
		return media;
	}

	public void setMedia(Double media) {
		this.media = media;
	}

	public Integer getPercentagem() {
		return percentagem;
	}

	public void setPercentagem(Integer percentagem) {
		this.percentagem = percentagem;
	}

	public Double getPercentual() {
		return percentual;
	}

	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}

	public String getAbc() {
		return Abc;
	}

	public void setAbc(String abc) {
		Abc = abc;
	}

	public Integer getDemanda() {
		return Demanda;
	}

	public void setDemanda(Integer demanda) {
		Demanda = demanda;
	}

	@Override
	public int compareTo(ItemAbcde o) {
		if (o.getMedia() < this.getMedia()  ) {
			return -1;
		} else if ( o.getMedia() > this.getMedia() ) {
			return 1;
		}

		return 0;
	}

	
}
