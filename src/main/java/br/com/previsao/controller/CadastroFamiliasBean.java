package br.com.previsao.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Familia;
import br.com.previsao.repository.Empresas;
import br.com.previsao.service.CadastroFamiliaService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroFamiliasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Familia familia;

	@Inject
	private Empresas empresas;

	private List<Empresa> empresasRaizes;

	public List<Empresa> getEmpresasRaizes() {
		return empresasRaizes;
	}

	@Inject
	private CadastroFamiliaService cadastroFamiliaService;

	public CadastroFamiliasBean() {

		limpar();

	}

	private void limpar() {
		familia = new Familia();

	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

			empresasRaizes = empresas.todasEmpresas();

		}

	}

	public void salvar() {
		try {
			cadastroFamiliaService.salvar(this.familia);

			limpar();

			FacesUtil.addInfoMessage("Familia " + FacesUtil.getMensagemI18n("salvo"));
		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());

		}
	}

	public boolean isEditando() {
		return this.familia.getCodigo() != null;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

}
