package br.com.previsao.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Telefone;
import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Empresas;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.service.CadastroGerenteRegionalService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEncarregadoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios gerentes;

	private Empresa empresa;

	@Inject
	private Empresas empresas;

	private List<Empresa> empresasRaizes;

	@Inject
	private CadastroGerenteRegionalService cadastroEncarregadoService;

	private Usuario encarregado;

	private List<Usuario> chefeGerentes;

	public CadastroEncarregadoBean() {
		limpar();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			empresasRaizes = empresas.todasEmpresas();

			if (this.empresa != null) {
				carregarDiretor();
			}
		}
	}

	public void carregarDiretor() {
		chefeGerentes = gerentes.carregaDiretor(empresa.getCodigo());
	}

	private void limpar() {
		encarregado = new Usuario();
		empresa = new Empresa();
		encarregado.setTelefone(new Telefone());
		;
	}

	public void salvar() {

		this.encarregado = cadastroEncarregadoService.salvarEncarregado(this.encarregado, empresa.getCodigo());

		limpar();

		FacesUtil.addInfoMessage(FacesUtil.getMensagemI18n("encarregado_salvo"));
	}

	public boolean isEditando() {
		return this.encarregado.getCodigo() != null;
	}

	public Usuario getEncarregado() {
		return encarregado;
	}

	public void setEncarregado(Usuario encarregado) {

		this.encarregado = encarregado;

		if (this.encarregado != null) {
			this.empresa = this.encarregado.getEmpresa(); // isto é usado para
															// ao editar
															// carregar as
															// empresas na tela
															// de cadastro

		}

	}

	public List<Usuario> getchefeGerentes() {
		return chefeGerentes;
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
