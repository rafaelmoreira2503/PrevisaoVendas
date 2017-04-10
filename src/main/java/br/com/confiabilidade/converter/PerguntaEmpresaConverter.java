package br.com.confiabilidade.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.confiabilidade.model.PerguntaEmpresa;
import br.com.confiabilidade.repository.PerguntaEmpresas;
import br.com.confiabilidade.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = PerguntaEmpresa.class,value="perguntaEmpresaConverter")
public class PerguntaEmpresaConverter implements Converter {

	// @Inject
	private PerguntaEmpresas perguntaempresas;

	public PerguntaEmpresaConverter() {
		perguntaempresas = CDIServiceLocator.getBean(PerguntaEmpresas.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		PerguntaEmpresa retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = perguntaempresas.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			PerguntaEmpresa perguntaempresa = (PerguntaEmpresa) value;
			return perguntaempresa.getCodigo() == null ? null : perguntaempresa
					.getCodigo().toString();

		}

		return "";
	}

}
