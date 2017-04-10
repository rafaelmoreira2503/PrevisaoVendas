package br.com.confiabilidade.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Usuario.class,value="usuarioConverter")
public class UsuarioConverter implements Converter {

	// @Inject
	private Usuarios usuarios;

	public UsuarioConverter() {
		usuarios = (Usuarios) CDIServiceLocator.getBean((Usuarios.class));
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Usuario retorno = null;

		if (value != null) {
			Long id = new Long(value);

			retorno = usuarios.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Usuario usuario = (Usuario) value;

			return usuario.getCodigo() == null ? null : usuario.getCodigo()
					.toString();
		}

		return "";
	}

}
