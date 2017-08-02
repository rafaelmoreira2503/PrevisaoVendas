package br.com.previsao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.previsao.model.Historico;
import br.com.previsao.repository.Historicos;
import br.com.previsao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Historico.class)
public class HistoricoConverter implements Converter {

	// @Inject
	private Historicos historicos;

	public HistoricoConverter() {
		historicos = (Historicos) CDIServiceLocator.getBean(Historicos.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Historico retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = historicos.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Historico historico = (Historico) value;
			return historico.getCodigo() == null ? null : historico.getCodigo().toString();
		}

		return "";
	}

}
