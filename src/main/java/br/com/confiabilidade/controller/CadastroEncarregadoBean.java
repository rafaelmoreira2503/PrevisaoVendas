package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Telefone;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Areas;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.service.CadastroEncarregadoService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEncarregadoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios gerentes;

	@Inject
	private Areas areas;

	private Empresa empresa;

	@Inject
	private Empresas empresas;

	private List<Area> areasdoEncarregado;

	private List<Empresa> empresasRaizes;

	@Inject
	private CadastroEncarregadoService cadastroEncarregadoService;

	private Usuario encarregado;

	private List<Usuario> chefeGerentes;

	public CadastroEncarregadoBean() {
		limpar();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			empresasRaizes = empresas.todasEmpresas();

			carregarArea();

			if (this.empresa != null) {
				carregarGerente();
			}
		}
	}

	public void carregarArea() {
		areasdoEncarregado = areas.temComoArea();
	}

	public void carregarGerente() {
		chefeGerentes = gerentes.carregaGerente(empresa.getCodigo());
	}

	private void limpar() {
		encarregado = new Usuario();
		empresa = new Empresa();
		encarregado.setTelefone(new Telefone());
		;
	}

	public void salvar() {

		this.encarregado = cadastroEncarregadoService.salvarEncarregado(
				this.encarregado, empresa.getCodigo());

		limpar();

		FacesUtil
				.addInfoMessage(FacesUtil.getMensagemI18n("encarregado_salvo"));
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

	public List<Area> getAreasdoEncarregado() {
		return areasdoEncarregado;
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
