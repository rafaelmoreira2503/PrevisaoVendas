package br.com.confiabilidade.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.repository.Perguntas;
import br.com.confiabilidade.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Pergunta.class,value="perguntaConverter")
public class PerguntaConverter implements Converter {

	// @Inject
	private Perguntas perguntas;

	public PerguntaConverter() {
		perguntas = CDIServiceLocator.getBean(Perguntas.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Pergunta retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = perguntas.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Pergunta pergunta = (Pergunta) value;
			return pergunta.getCodigo() == null ? null : pergunta.getCodigo()
					.toString();

		}

		return "";
	}

}
