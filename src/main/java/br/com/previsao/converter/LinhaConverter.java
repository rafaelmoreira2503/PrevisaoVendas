package br.com.previsao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.previsao.model.Linha;
import br.com.previsao.repository.Linhas;
import br.com.previsao.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = Linha.class)
public class LinhaConverter implements Converter {

	// @Inject
	private Linhas linhas;

	public LinhaConverter() {
		linhas = (Linhas) CDIServiceLocator.getBean(Linhas.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Linha retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = linhas.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Linha linha = (Linha) value;
			return linha.getCodigo() == null ? null : linha.getCodigo()
					.toString();
		}

		return "";
	}

}
