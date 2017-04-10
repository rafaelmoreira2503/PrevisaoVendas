package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.PerguntaEmpresa;
import br.com.confiabilidade.repository.PerguntaEmpresas;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CadastroPerguntaEmpresaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerguntaEmpresas perguntaEmpresas;

	@Transactional
	public void salvar(PerguntaEmpresa perguntaEmpresa) {

		PerguntaEmpresa perguntasAssociadasExistentes = perguntaEmpresas
				.buscaPerguntaExistente(perguntaEmpresa.getCodigo_empresa(),
						perguntaEmpresa.getCodigo_pergunta());

		if (perguntasAssociadasExistentes != null) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("pergunta_empresa_existente"));
		}
		perguntaEmpresas.guardar(perguntaEmpresa);

	}

	@Transactional
	public void associar(PerguntaEmpresa perguntaEmpresa, Item item) {

		if (perguntaEmpresa.getResposta() == null) {
			throw new NegocioException(FacesUtil.getMensagemI18n("resposta_obrigatoria"));
		}

		PerguntaEmpresa perguntasdaCriticaTemp = perguntaEmpresas
				.buscaItemExistente(perguntaEmpresa.getCodigo_empresa()
						.getCodigo(), perguntaEmpresa.getCodigo_pergunta()
						.getCodigo(), item.getCodigo(), item.getAreaItem()
						.getCodigo());

		

		if (perguntasdaCriticaTemp == null) {// NÃO EXISTE
												// pergunta inclui um novo
												// objeto

			PerguntaEmpresa perguntaEmpresaTemp = new PerguntaEmpresa();

			perguntaEmpresaTemp.setCodigo_empresa(perguntaEmpresa
					.getCodigo_empresa());

			perguntaEmpresaTemp.setCodigo_pergunta(perguntaEmpresa
					.getCodigo_pergunta());

			perguntaEmpresaTemp.setResposta(perguntaEmpresa.getResposta());

			perguntaEmpresaTemp.setItemCriticado(item);

			perguntaEmpresaTemp.setAreaCriticada(item.getAreaItem());

			perguntaEmpresas.guardar(perguntaEmpresaTemp);

		} else {// É o mesmo Objeto e já existe uma critica para este item
			
			//poderia colocar perguntaEmpresa ao invés de perguntaEmpresaTemp ? !!!!!!!!!!!!!

			// UPDATE POIS EU QUERO DAR UPDATE NA RESPOSTA SELECIONADA

			perguntasdaCriticaTemp.setCodigo_empresa(perguntaEmpresa
					.getCodigo_empresa());
			perguntasdaCriticaTemp.setCodigo_pergunta(perguntaEmpresa
					.getCodigo_pergunta());
			perguntasdaCriticaTemp.setResposta(perguntaEmpresa.getResposta());

			perguntasdaCriticaTemp.setItemCriticado(perguntasdaCriticaTemp
					.getItemCriticado());
			perguntaEmpresas.guardar(perguntasdaCriticaTemp);

		}

	}
}