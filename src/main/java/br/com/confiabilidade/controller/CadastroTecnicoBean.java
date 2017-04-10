package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.model.Telefone;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.Especialidades;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.service.CadastroTecnicoService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroTecnicoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Especialidades especialidades;

	@Inject
	private Usuarios encarregados;

	@Inject
	private Empresas empresas;

	private Empresa empresa;

	private List<Empresa> empresasRaizes;

	@Inject
	private CadastroTecnicoService cadastroTecnicoService;

	private Usuario tecnico;

	private List<Usuario> chefeEncarregados;

	private List<Especialidade> especialidadesRaizes;

	public CadastroTecnicoBean() {
		limpar();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

			empresasRaizes = empresas.todasEmpresas();

			especialidadesRaizes = especialidades.todasEspecialidades();

			if (this.empresa != null) {
				carregarEncarregado();

			}

		}
	}

	public void carregarEncarregado() {
		
		chefeEncarregados = encarregados.carregarEncarregado(empresa
				.getCodigo());
	}

	private void limpar() {
		tecnico = new Usuario();
		empresa = new Empresa();
		tecnico.setTelefone(new Telefone());
		chefeEncarregados = new ArrayList<>();
	}

	public void salvar() {

		this.tecnico = cadastroTecnicoService.salvarTecnico(this.tecnico);

		

		FacesUtil.addInfoMessage("Técnico " +tecnico.getNome() +"  "
				+ FacesUtil.getMensagemI18n("salvo"));
		limpar();
	}

	public boolean isEditando() {
		return this.tecnico.getCodigo() != null;
	}

	public Usuario getTecnico() {
		return tecnico;
	}

	public void setTecnico(Usuario tecnico) {
		this.tecnico = tecnico;

		if (this.tecnico != null) {
			this.empresa = this.tecnico.getEmpresa(); // isto é usado para ao
														// editar carregar as
														// empresas na tela de
														// cadastro
			
		}
	}

	public List<Especialidade> getEspecialidadesRaizes() {
		return especialidadesRaizes;
	}

	public List<Usuario> getChefeEncarregados() {
		return chefeEncarregados;
	}

	@NotNull
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Empresa> getEmpresasRaizes() {
		return empresasRaizes;
	}

}
