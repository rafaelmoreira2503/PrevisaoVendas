package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.confiabilidade.model.ItemAbc;
import br.com.confiabilidade.repository.ItemsAbc;
import br.com.confiabilidade.util.jpa.Transactional;

public class CadastroItemAbcService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ItemsAbc itemsAbc;

	@Transactional
	public void salvar(ItemAbc itemAbc) { // Associo o itemAbc ao Tecnico

		ItemAbc itemAbcTemp = itemsAbc.buscaItemAbcExistente(itemAbc);

		if (itemAbcTemp == null) {

			itemsAbc.guardar(itemAbc);
		} else {
			itemAbcTemp.setAbc(itemAbc.getAbc());
			itemAbcTemp.setCodigoItemAbc(itemAbc.getCodigoItemAbc());
			itemAbcTemp.setDataItemAbc(itemAbc.getDataItemAbc());
			itemAbcTemp.setDescricaoAbc(itemAbc.getDescricaoAbc());
			itemAbcTemp.setMedia(itemAbc.getMedia());
			itemAbcTemp.setDemanda(itemAbc.getDemanda());
			itemAbcTemp.setPercentagem(itemAbc.getPercentagem());
			itemAbcTemp.setPercentual(itemAbc.getPercentual());

			itemsAbc.guardar(itemAbcTemp);

		}

	}

	@Transactional
	public void salvarTempoDeReposicao(ItemAbc itemAbc) {

		if (itemAbc.getTempoDeReposicao().getTempoReposicaoImportado() == null) {
			itemAbc.getTempoDeReposicao().setTempoReposicaoImportado(0);
		}

		calculaTempo(itemAbc);
		calculaQuantidadeRepor(itemAbc);
		calculaPontoDeReposicao(itemAbc);
		calculaEstoqueDeSeguranca(itemAbc);
		calculaEstoqueMaximo(itemAbc);
		itemsAbc.guardar(itemAbc);

	}

	void calculaTempo(ItemAbc itemAbc) {
		itemAbc.getTempoDeReposicao()
				.setTempoReposicao(
						round2(((double) itemAbc.getTempoDeReposicao()
								.getTempoReposicaoCompras()
								+ itemAbc.getTempoDeReposicao()
										.getTempoReposicaoFornecedor()
								+ itemAbc.getTempoDeReposicao()
										.getTempoReposicaoImportado()
								+ itemAbc.getTempoDeReposicao()
										.getTempoReposicaoRecebimento()
								+ itemAbc.getTempoDeReposicao()
										.getTempoReposicaoRequisicao() + itemAbc
								.getTempoDeReposicao()
								.getTempoReposicaoTransporte()) / 30.0));

	}

	private void calculaQuantidadeRepor(ItemAbc itemAbc) {

		Double quantidadeaRepor = 0.0;

		if (("D").equals(itemAbc.getAbc()) || ("E").equals(itemAbc.getAbc())
				|| itemAbc.getTempoDeReposicao() == null) {
			quantidadeaRepor = 0.0;
		} else {

			if (("A").equals(itemAbc.getAbc())
					&& itemAbc.getTempoDeReposicao().getTempoReposicao() >= 1.0) {

				quantidadeaRepor = itemAbc.getDemanda()
						* itemAbc.getTempoDeReposicao().getTempoReposicao();

			} else if (("A").equals(itemAbc.getAbc())) {
				quantidadeaRepor = itemAbc.getDemanda() * 1.0;
				System.out.println("A");
			}
			if (("B").equals(itemAbc.getAbc())
					&& itemAbc.getTempoDeReposicao().getTempoReposicao() >= 1.5) {

				quantidadeaRepor = itemAbc.getDemanda()
						* itemAbc.getTempoDeReposicao().getTempoReposicao();

			} else if (("B").equals(itemAbc.getAbc())) {
				quantidadeaRepor = itemAbc.getDemanda() * 1.5;
				System.out.println("B");
			}

			if (("C").equals(itemAbc.getAbc())
					&& itemAbc.getTempoDeReposicao().getTempoReposicao() >= 2.0) {

				quantidadeaRepor = itemAbc.getDemanda()
						* itemAbc.getTempoDeReposicao().getTempoReposicao();

			} else if (("C").equals(itemAbc.getAbc())) {

				quantidadeaRepor = itemAbc.getDemanda() * 2.0;
				System.out.println("C");
			}
		}
		itemAbc.setQuantidadeRepor(Math.round(quantidadeaRepor));
		System.out.println("Tempo QR " + Math.round(quantidadeaRepor));
	}

	private void calculaEstoqueDeSeguranca(ItemAbc itemAbc) {
		Double estoquedeSeguranca = 0.0;

		if (itemAbc.getItem() == null && "A".equals(itemAbc.getAbc())) {

			estoquedeSeguranca = itemAbc.getDemanda() / 8;
		} else {

			if (("A").equals(itemAbc.getAbc())
					&& ("X").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = itemAbc.getDemanda();

			}
			if (("A").equals(itemAbc.getAbc())
					&& ("Y").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = itemAbc.getDemanda() / 8;

			}
			if (("A").equals(itemAbc.getAbc())
					&& ("Z").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = 0.0;

			}

		}

		if (itemAbc.getItem() == null && "B".equals(itemAbc.getAbc())) {

			estoquedeSeguranca = itemAbc.getDemanda() / 4;

		} else {
			if (("B").equals(itemAbc.getAbc())
					&& ("X").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = itemAbc.getDemanda() / 2;

			}
			if (("B").equals(itemAbc.getAbc())
					&& ("Y").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = itemAbc.getDemanda() / 4;

			}
			if (("B").equals(itemAbc.getAbc())
					&& ("Z").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = 0.0;

			}

		}

		if (itemAbc.getItem() == null && "C".equals(itemAbc.getAbc())) {

			estoquedeSeguranca = itemAbc.getDemanda() / 2;

		} else {
			if (("C").equals(itemAbc.getAbc())
					&& ("X").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = itemAbc.getDemanda();

			}
			if (("C").equals(itemAbc.getAbc())
					&& ("Y").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = itemAbc.getDemanda() / 2;

			}
			if (("C").equals(itemAbc.getAbc())
					&& ("Z").equals(itemAbc.getItem().getCriticidade())) {

				estoquedeSeguranca = 0.0;

			}

		}
		if (itemAbc.getItem() == null
				&& ("D".equals(itemAbc.getAbc()) || "E"
						.equals(itemAbc.getAbc()))) {
			estoquedeSeguranca = 0.0;

		}

		itemAbc.setEstoqueDeSeguranca(Math.round(estoquedeSeguranca));
		System.out.println("Tempo Estoque de Segurança "
				+ Math.round(estoquedeSeguranca));
	}

	private void calculaPontoDeReposicao(ItemAbc itemAbc) {
		Double pontoDeReposicao = 0.0;
		if (("D".equals(itemAbc.getAbc()) || "E".equals(itemAbc.getAbc()) || itemAbc
				.getTempoDeReposicao() == null)) {
			pontoDeReposicao = 0.0;
		} else {

			if (itemAbc.getTempoDeReposicao().getTempoReposicao() != null) {
				pontoDeReposicao = itemAbc.getDemanda()
						* itemAbc.getTempoDeReposicao().getTempoReposicao();
			}
		}
		itemAbc.setPontoDeReposicao(Math.round(pontoDeReposicao));
		System.out.println("Tempo Ponto Reposicao "
				+ Math.round(pontoDeReposicao));
	}

	private void calculaEstoqueMaximo(ItemAbc itemAbc) {
		Long estoqueMaximo = 0L;

		estoqueMaximo = (long) (itemAbc.getPontoDeReposicao().longValue()
				+ itemAbc.getQuantidadeRepor() + itemAbc
				.getEstoqueDeSeguranca());

		itemAbc.setEstoqueMaximo(estoqueMaximo);

		System.out.println("Estoque máximoo " + estoqueMaximo);

	}

	double round2(double x) { // Arredonda 2 digitos

		final double factor = 1e2;

		return (long) (x * factor + 0.5) / factor;

	}
}
