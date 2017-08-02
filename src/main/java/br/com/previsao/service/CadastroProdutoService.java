package br.com.previsao.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.previsao.model.Produto;
import br.com.previsao.repository.Produtos;
import br.com.previsao.util.jpa.Transactional;
import br.com.previsao.util.jsf.FacesUtil;

public class CadastroProdutoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Produtos produtos;

	@Transactional
	public void salvar(Produto produto) {
		// Produto produtoExistente = produtos.porCodigo(produto);
		// if (produtoExistente != null && !produtoExistente.equals(produto)) {
		//
		// }

		try {
			this.produtos.guardar(produto);
		} catch (OptimisticLockException e) {

			throw new NegocioException(FacesUtil.getMensagemI18n("concorrencia_familia"));

		}

	}

	@Transactional
	public void importar(Produto produto) {
		Produto produtoExistente = produtos.porCodigoeEmpresa(produto);
		if (produtoExistente == null) {
			try {
				this.produtos.guardar(produto);
			} catch (OptimisticLockException e) {

				throw new NegocioException(FacesUtil.getMensagemI18n("concorrencia_familia"));

			}

		}
	}
}