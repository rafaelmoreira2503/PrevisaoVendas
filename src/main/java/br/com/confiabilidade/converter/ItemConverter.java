package br.com.confiabilidade.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Item.class)
public class ItemConverter implements Converter {

	// @Inject
	private Items items;

	public ItemConverter() {
		items = (Items) CDIServiceLocator.getBean(Items.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Item retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = items.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			Item item = (Item) value;
			return item.getCodigo() == null ? null : item.getCodigo()
					.toString();
		}

		return "";
	}

}
