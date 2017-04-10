package br.com.confiabilidade.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Justificativa;
import br.com.confiabilidade.model.StatusItem;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CadastroItemService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Items items;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	@Transactional
	public void importar(Item item) {

		items.guardar(item);

	}

	@Transactional
	public void salvar(Item item) { // Associo o item ao Tecnico

		Item itemExistente = items.buscaItemComTecnicoExistente(item
				.getCodigo(), item.getAreaItem().getCodigo(), item.getTecnico()
				.getCodigo());

		if (itemExistente != null && !itemExistente.equals(item)) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("tecnico_item_existente"));
		}

		item.setNaoAplicavel(false);
		item.setJustificativa(null);

		item.setStatus(StatusItem.ASSOCIADO);

		item.setDataItem(new Date());
		items.guardar(item);

	}

	@Transactional
	public void naoAplicar(Item item) { // Associo o item ao Tecnico
		if (item.isNaoAplicavel()) { // =1
			item.setNaoAplicavel(true);
			item.setJustificativa(Justificativa.OUTRAAREA);
			item.setTecnico(null);
			item.setStatus(StatusItem.NAOSEAPLICA);

			FacesUtil.addInfoMessage("Item " + item.getDescricao()
					+ FacesUtil.getMensagemI18n("nao_aplicavel"));

		} else {
			item.setNaoAplicavel(false);
			item.setJustificativa(null);
			item.setTecnico(null);
			item.setStatus(StatusItem.PENDENTE);
			FacesUtil.addInfoMessage("Item" + item.getDescricao()
					+ FacesUtil.getMensagemI18n("aplicavel"));

		}

		item.setDataItem(new Date());
		items.guardar(item);

	}

	@Transactional
	public void justificar(Item item) {
		// item = this.items.porId(item.getCodigo());

		item.setStatus(StatusItem.JUSTIFICADO);

		item.setDataItem(new Date());

		item = this.items.guardar(item);

	}

	@Transactional
	public void limpar(Item item) {
		// item = this.items.porId(item.getCodigo());

		if (item.getStatus().equals(StatusItem.CRITICADO)) {

			item.setStatus(StatusItem.ASSOCIADO);
			item.setCriticidade(null);
			item.setDataItem(new Date());

		} else {

			item.setJustificativa(null);
			item.setStatus(StatusItem.ASSOCIADO);
			item.setDataItem(new Date());
		}

		item = this.items.guardar(item);

	}

	@Transactional
	public void criticar(Item item) {
		// item = this.items.porId(item.getCodigo());

		item.setStatus(StatusItem.CRITICADO);
		item.setDataItem(new Date());
		this.items.guardar(item);

	}

}