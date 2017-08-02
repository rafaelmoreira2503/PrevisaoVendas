package br.com.previsao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Familia;
import br.com.previsao.modelolazy.FamiliaLazyList;
import br.com.previsao.repository.Empresas;
import br.com.previsao.repository.Familias;
import br.com.previsao.service.CadastroFamiliaService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaFamiliasBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Familias familias;

	@Inject
	private Empresas empresas;

	private List<Familia> listaDeFamilias = new ArrayList<>();

	private Familia familiaSelecionada;

	private List<Familia> familiasSelecionadas = new ArrayList<>();

	public List<Familia> getListaDeFamilias() {
		return listaDeFamilias;
	}

	public FamiliaLazyList getLazyFamilias() {
		return lazyFamilias;
	}

	public void setListaDeFamilias(List<Familia> listaDeFamilias) {
		this.listaDeFamilias = listaDeFamilias;
	}

	public void setLazyFamilias(FamiliaLazyList lazyFamilias) {
		this.lazyFamilias = lazyFamilias;
	}

	private List<Empresa> empresasRaizes = new ArrayList<>();

	public List<Empresa> getEmpresasRaizes() {
		return empresasRaizes;
	}

	@Inject
	private CadastroFamiliaService cadastroFamiliaService;

	private Familia novaFamilia = new Familia();

	public Familia getNovaFamilia() {
		return novaFamilia;
	}

	public void setNovaFamilia(Familia novaFamilia) {
		this.novaFamilia = novaFamilia;
	}

	private FamiliaLazyList lazyFamilias;

	public List<Familia> getFamilias() {
		return listaDeFamilias;
	}

	public void inicializar() {

		if (FacesUtil.isNotPostback()) {
			lazyFamilias = new FamiliaLazyList(familias);
			empresasRaizes = empresas.todasEmpresas();

		}

	}

	public void doCreateFamlia() {
		cadastroFamiliaService.salvar(novaFamilia);
	}

	public void doUpdateFamilia(ActionEvent actionEvent) {
		
		FacesUtil.addInfoMessage("Familia " + familiaSelecionada.getEmpresa().getNome());
		
		cadastroFamiliaService.salvar(familiaSelecionada);
	}

	public void doDeleteFamilias() {

		for (Familia fami : listaDeFamilias) {
			System.out.println(fami.getDescricao());
		}
		familias.removerFamilias(listaDeFamilias);
	}

	public void excluir() {
		familias.remover(familiaSelecionada);
		this.listaDeFamilias.remove(familiaSelecionada);
		FacesUtil.addInfoMessage("Familia " + familiaSelecionada.getDescricao() + " excluída com sucesso.");
	}

	public Familia getFamiliaSelecionada() {
		return familiaSelecionada;
	}

	public void setFamiliaSelecionada(Familia familiaSelecionada) {
		this.familiaSelecionada = familiaSelecionada;
	}

	public List<Familia> getFamiliasSelecionadas() {
		return familiasSelecionadas;
	}

	public void setFamiliasSelecionadas(List<Familia> familiasSelecionadas) {
		this.familiasSelecionadas = familiasSelecionadas;
	}

	public FamiliaLazyList getLazyModel() {
		return lazyFamilias;
	}

}
