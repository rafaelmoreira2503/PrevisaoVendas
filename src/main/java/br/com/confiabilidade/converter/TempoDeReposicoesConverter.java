package br.com.confiabilidade.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.confiabilidade.model.TempoDeReposicao;
import br.com.confiabilidade.repository.TempoDeReposicoes;
import br.com.confiabilidade.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = TempoDeReposicao.class)
public class TempoDeReposicoesConverter implements Converter {

	// @Inject
	private TempoDeReposicoes tempoDeReposicoes;

	public TempoDeReposicoesConverter() {
		tempoDeReposicoes = (TempoDeReposicoes) CDIServiceLocator
				.getBean(TempoDeReposicoes.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		TempoDeReposicao retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = tempoDeReposicoes.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			TempoDeReposicao tempoDEReposicao = (TempoDeReposicao) value;
			return tempoDEReposicao.getCodigo() == null ? null
					: tempoDEReposicao.getCodigo().toString();
		}

		return "";
	}

}
