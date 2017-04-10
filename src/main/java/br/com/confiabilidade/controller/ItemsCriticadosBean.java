package br.com.confiabilidade.controller;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.modelolazy.LazyItemsCriticados;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.security.UsuarioLogado;

@Named
@ViewScoped
public class ItemsCriticadosBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Items Items;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	private LazyItemsCriticados lazyItems;

	public void inicializar() {

		
		lazyItems = new LazyItemsCriticados(Items);

	}

	public Long getTotalFeito() {
		return Items.buscaFeitoParaGerente(usuario.getEmpresa().getCodigo());

	}

	public Long getTotal() {
		return Items.buscaTotalParaGerente(usuario.getEmpresa().getCodigo());

	}

	public LazyItemsCriticados getLazyModel() {
		return lazyItems;
	}

}
