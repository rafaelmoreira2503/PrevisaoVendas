package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.StatusItem;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.modelolazy.LazyAssociaItems;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.repository.filter.AssociaItemFilter;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.CadastroItemService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class AssociaItemsBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Items items;

	@Inject
	private Usuarios tecnicos;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	private AssociaItemFilter filtro;

	private Usuario tecnico;

	@Inject
	private CadastroItemService cadastroItemService;

	private Item itemSelecionado;

	private List<Usuario> tecnicosDaEmpresa;

	private LazyAssociaItems lazyItems;

	public AssociaItemsBean() {
		filtro = new AssociaItemFilter();
		

	}

	public void pesquisar() {

		tecnicosDaEmpresa = tecnicos.tecnicosdoEncarregado(usuario.getCodigo());

		lazyItems = new LazyAssociaItems(items, filtro);

	}

	public AssociaItemFilter getFiltro() {
		return filtro;
	}

	public void selecionar(Item item) {

		cadastroItemService.naoAplicar(item);

	}

	public void limpar() {

		filtro = new AssociaItemFilter();

	}

	public void associar() {

		cadastroItemService.salvar(this.itemSelecionado);

		FacesUtil.addInfoMessage("Item "+  FacesUtil.getMensagemI18n("salvo"));

	}

	public Long getTotalFeito() {
		return items.buscaTotalFeitoParaEncarregado(usuario
				.getAreaEncarregado().getCodigo(), usuario.getEmpresa()
				.getCodigo());

	}

	public Long getTotal() {
		return items.buscaTotalParaEncarregado(usuario.getAreaEncarregado()
				.getCodigo(), usuario.getEmpresa().getCodigo());

	}

	public Usuario getTecnico() {
		return tecnico;
	}

	public void setTecnico(Usuario tecnico) {
		this.tecnico = tecnico;
	}

	public Item getItemSelecionado() {

		return itemSelecionado;
	}

	public void setItemSelecionado(Item itemSelecionado) {

		this.itemSelecionado = itemSelecionado;
	}

	public LazyAssociaItems getLazyModel() {
		return lazyItems;
	}

	public List<Usuario> getTecnicosDaEmpresa() {
		return tecnicosDaEmpresa;
	}

	public StatusItem[] getStatuses() {
		return StatusItem.values();
	}

}
