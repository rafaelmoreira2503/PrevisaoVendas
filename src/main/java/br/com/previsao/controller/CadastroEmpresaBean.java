package br.com.previsao.controller;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Endereco;
import br.com.previsao.service.CadastroEmpresaService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEmpresaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Empresa empresa;

	@Inject
	private CadastroEmpresaService cadastroEmpresaService;

	public CadastroEmpresaBean() {

		limpar();
	}

	private void limpar() {

		empresa = new Empresa();
		empresa.setEndereco(new Endereco());

	}

	public void salvar() {

		this.empresa = cadastroEmpresaService.salvar(this.empresa);
		limpar();

		FacesUtil
				.addInfoMessage("Empresa" + FacesUtil.getMensagemI18n("salva"));
	}

	public boolean isEditando() {
		return this.empresa.getCodigo() != null;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}
