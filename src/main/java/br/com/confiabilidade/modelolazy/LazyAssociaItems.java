package br.com.confiabilidade.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.repository.filter.AssociaItemFilter;

public class LazyAssociaItems extends LazyDataModel<Item> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Items items;

	private AssociaItemFilter filtros;

	public LazyAssociaItems(Items items, AssociaItemFilter filtros) {
		this.items = items;
		this.filtros = filtros;
	}

	@Override
	public List<Item> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {
		List<Item> itemsLazy = this.items.buscaPorPaginacao(first, pageSize,
				sortField, sortOrder, filtros);

		this.setRowCount(this.items.countAll(filtros));

		return itemsLazy;
	}
	
	@Override
	public Object getRowKey(Item Item) {
		return Item.getCodigo();
	}

	

	@Override
	public void setRowIndex(int rowIndex) {
		// solução para evitar ArithmeticException
		if (rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else
			super.setRowIndex(rowIndex % getPageSize());
	}

}
