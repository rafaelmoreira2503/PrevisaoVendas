package br.com.confiabilidade.controller;

import java.io.Serializable;

import br.com.confiabilidade.model.Empresa;

public class FiltroEmpresa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Empresa empresa;

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}
