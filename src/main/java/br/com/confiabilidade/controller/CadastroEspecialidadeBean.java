package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;

import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.modelolazy.EspecialidadeLazyList;
import br.com.confiabilidade.repository.Especialidades;
import br.com.confiabilidade.service.CadastroEspecialidadeService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEspecialidadeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Especialidade especialidade;

	@Inject
	private Especialidades especialidades;

	private List<Especialidade> listaDeEspecialidades = new ArrayList<>();

	private EspecialidadeLazyList lazyEspecialidades;

	private Especialidade especialidadeSelecionada;

	public List<Especialidade> getEspecialidades() {
		return listaDeEspecialidades;
	}

	public void inicializar() {

		lazyEspecialidades = new EspecialidadeLazyList(especialidades);

	}

	public void excluir() {

		especialidades.remover(especialidadeSelecionada);
		this.listaDeEspecialidades.remove(especialidadeSelecionada);

		FacesUtil.addInfoMessage("Especialidade "
				+ especialidadeSelecionada.getDescricao()
				+ " excluída com sucesso.");
	}

	@Inject
	private CadastroEspecialidadeService cadastroEspecialidadeService;

	public CadastroEspecialidadeBean() {
		limpar();
	}

	private void limpar() {
		especialidade = new Especialidade();

	}

	public void salvar() {

		cadastroEspecialidadeService.salvar(this.especialidade);
		
		limpar();

		FacesUtil.addInfoMessage("Especialidade"
				+ FacesUtil.getMensagemI18n("salvo"));
	}

	public boolean isEditando() {
		return this.especialidade.getCodigo() != null;
	}

	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	public Especialidade getEspecialidadeSelecionada() {
		return especialidadeSelecionada;
	}

	public void setEspecialidadeSelecionada(
			Especialidade especialidadeSelecionada) {
		this.especialidadeSelecionada = especialidadeSelecionada;
	}

	public EspecialidadeLazyList getLazyModel() {
		return lazyEspecialidades;
	}

	public void setLazyEspecialidades(EspecialidadeLazyList lazyEspecialidades) {
		this.lazyEspecialidades = lazyEspecialidades;
	}
}
