package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.modelolazy.EspecialidadeLazyList;
import br.com.confiabilidade.repository.Especialidades;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEspecialidadesBean implements Serializable {

	private static final long serialVersionUID = 1L;

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

	public Especialidade getEspecialidadeSelecionada() {
		return especialidadeSelecionada;
	}

	

	public void setEspecialidadeSelecionada(Especialidade especialidadeSelecionada) {
		this.especialidadeSelecionada = especialidadeSelecionada;
	}

	public EspecialidadeLazyList getLazyModel() {
		return lazyEspecialidades;
	}

}
