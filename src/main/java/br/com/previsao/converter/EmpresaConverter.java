package br.com.previsao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.previsao.model.Empresa;
import br.com.previsao.repository.Empresas;
import br.com.previsao.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Empresa.class, value = "empresaConverter")
public class EmpresaConverter implements Converter {

	// @Inject
	private Empresas empresas;

	public EmpresaConverter() {
		empresas = CDIServiceLocator.getBean(Empresas.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Empresa retorno = null;

		if (value != null) {
			Long id = new Long(value);

			retorno = empresas.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Empresa empresa = (Empresa) value;

			return empresa.getCodigo() == null ? null : empresa.getCodigo().toString();
		}

		return "";
	}

}
