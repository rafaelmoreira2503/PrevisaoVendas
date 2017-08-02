package br.com.previsao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.previsao.model.Familia;
import br.com.previsao.repository.Familias;
import br.com.previsao.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = Familia.class)
public class FamiliaConverter implements Converter {

	// @Inject
	private Familias familias;

	public FamiliaConverter() {
		familias = (Familias) CDIServiceLocator.getBean(Familias.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Familia retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = familias.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Familia familia = (Familia) value;
			return familia.getCodigo() == null ? null : familia.getCodigo()
					.toString();
		}

		return "";
	}

}
