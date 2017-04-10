package br.com.confiabilidade.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.repository.Especialidades;
import br.com.confiabilidade.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Especialidade.class)
public class EspecialidadeConverter implements Converter {

	// @Inject
	private Especialidades especialidades;

	public EspecialidadeConverter() {
		especialidades = (Especialidades) CDIServiceLocator.getBean(Especialidades.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Especialidade retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = especialidades.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Especialidade especialidade = (Especialidade) value;
			return especialidade.getCodigo() == null ? null : especialidade.getCodigo().toString();
		}
		
		return "";
	}

}
