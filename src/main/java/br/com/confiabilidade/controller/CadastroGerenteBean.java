package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Telefone;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.service.CadastroGerenteService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroGerenteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;

	@Inject
	private CadastroGerenteService cadastroGerenteService;

	private Usuario gerente;

	private List<Empresa> empresasRaizes;

	public CadastroGerenteBean() {
		limpar();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

			empresasRaizes = empresas.todasEmpresas();

		}
	}

	private void limpar() {
		gerente = new Usuario();

		gerente.setTelefone(new Telefone());
	}

	public void salvar() {

		this.gerente = cadastroGerenteService.salvar(this.gerente);

		limpar();

		FacesUtil
				.addInfoMessage("Gerente " + FacesUtil.getMensagemI18n("salvo"));
	}

	public boolean isEditando() {
		return this.gerente.getCodigo() != null;
	}

	public Usuario getGerente() {
		return gerente;
	}

	public void setGerente(Usuario gerente) {
		this.gerente = gerente;
	}

	public List<Empresa> getEmpresasRaizes() {
		return empresasRaizes;
	}

}
