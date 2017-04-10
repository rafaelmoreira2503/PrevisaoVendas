package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.modelolazy.AreaLazyList;
import br.com.confiabilidade.repository.Areas;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaAreasBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Areas areas;

	private List<Area> listaDeAreas;

	

	private Area areaSelecionada;

	private AreaLazyList lazyAreas;

	public List<Area> getAreas() {
		return listaDeAreas;
	}

	public void inicializar() {

		lazyAreas = new AreaLazyList(areas);

	}

	public void excluir() {
		areas.remover(areaSelecionada);
		this.listaDeAreas.remove(areaSelecionada);
		FacesUtil.addInfoMessage("Area " + areaSelecionada.getDescricao()
				+ " excluída com sucesso.");
	}

	public Area getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

	public AreaLazyList getLazyModel() {
		return lazyAreas;
	}

	

}
