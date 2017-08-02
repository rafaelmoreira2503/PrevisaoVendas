package br.com.previsao.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.previsao.model.Produto;
import br.com.previsao.model.Usuario;
import br.com.previsao.model.vo.Filtro;
import br.com.previsao.repository.Produtos;
import br.com.previsao.security.UsuarioLogado;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PrevisaoDiretorBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Produtos previsoes;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	private List<Filtro> listaDePrevisoes = new ArrayList<>();

	public List<Filtro> getPrevisoes() {
		return listaDePrevisoes;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

			listaDePrevisoes = previsoes.listarProdutosDoDiretor(usuario.getEmpresa().getCodigo());

		}
	}

	public List<Filtro> getListaDePrevisoes() {
		return listaDePrevisoes;
	}

	public void setListaDePrevisoes(List<Filtro> listaDePrevisoes) {
		this.listaDePrevisoes = listaDePrevisoes;
	}

	public Double getTotal() {
		
		Double totalReceita = previsoes.buscaTotalParaDiretor(usuario.getEmpresa().getCodigo());
		return totalReceita;
	}

}
