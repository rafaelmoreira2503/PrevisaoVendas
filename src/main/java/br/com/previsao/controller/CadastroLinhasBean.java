package br.com.previsao.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Familia;
import br.com.previsao.model.Linha;
import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Empresas;
import br.com.previsao.repository.Familias;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.service.CadastroLinhaService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroLinhasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Linha linha;

	private Empresa empresa;

	@Inject
	private Empresas empresas;

	private List<Empresa> empresasRaizes;

	@Inject
	private Familias familias;

	private List<Familia> familiasRaizes;
	
	@Inject
	private Usuarios gerenteFiliais;

	private List<Usuario> usuariosRaizes;

	public List<Usuario> getUsuariosRaizes() {
		return usuariosRaizes;
	}

	@Inject
	private CadastroLinhaService cadastroLinhaService;

	public CadastroLinhasBean() {

		limpar();

	}

	private void limpar() {
		linha = new Linha();

	}

	public void salvar() {
		try {
			cadastroLinhaService.salvar(this.linha);

			limpar();

			FacesUtil.addInfoMessage("Linha " + FacesUtil.getMensagemI18n("salvo"));
		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());

		}
	}

	public void inicializar() {

		if (FacesUtil.isNotPostback()) {
			empresasRaizes = empresas.todasEmpresas();

			if (empresa != null) {
				carregarFamilia();
				carregarGerentesFiliais();
			}
		}
	}

	public void carregarFamilia() {
		familiasRaizes = familias.carregaFamilia(empresa.getCodigo());

	}
	
	public void carregarGerentesFiliais() {
	     usuariosRaizes = gerenteFiliais.carregarGerentesFiliais(empresa.getCodigo());

	}

	public boolean isEditando() {
		return this.linha.getCodigo() != null;
	}

	public Linha getLinha() {
		return linha;
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
		if (this.linha != null) {

			this.empresa = linha.getFamilia().getEmpresa(); // isto é usado para
			// ao editar
			// carregar as
			// empresas na tela
			// de cadastro
		}
	}

	public List<Familia> getFamiliasRaizes() {
		return familiasRaizes;
	}

	public List<Empresa> getEmpresasRaizes() {
		return empresasRaizes;
	}

	@NotNull
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}
