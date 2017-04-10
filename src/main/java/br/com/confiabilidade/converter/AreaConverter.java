package br.com.confiabilidade.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.repository.Areas;
import br.com.confiabilidade.util.cdi.CDIServiceLocator;


@FacesConverter(forClass = Area.class)
public class AreaConverter implements Converter {

	// @Inject
	private Areas areas;

	public AreaConverter() {
		areas = (Areas) CDIServiceLocator.getBean(Areas.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Area retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = areas.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Area area = (Area) value;
			return area.getCodigo() == null ? null : area.getCodigo()
					.toString();
		}

		return "";
	}

}
