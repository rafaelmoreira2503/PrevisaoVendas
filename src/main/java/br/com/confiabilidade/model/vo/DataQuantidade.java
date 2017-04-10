package br.com.confiabilidade.model.vo;

import java.io.Serializable;
import java.util.Date;

public class DataQuantidade implements Serializable {

	private static final long serialVersionUID = 1L;

	public DataQuantidade() {
		super();
		
	}

	private Date data;
	private Long quantidade;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

}
