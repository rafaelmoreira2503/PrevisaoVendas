package br.com.confiabilidade.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.repository.Items;

public class ItemLazyList extends LazyDataModel<Item> implements
		SelectableDataModel<Item>, Serializable {

	private static final long serialVersionUID = 1L;

	// Data Source for binding data to the DataTable
	private List<Item> itemsLazy;

	private Items items;

	public ItemLazyList(Items items) {
		this.items = items;
	}

	public List<Item> load(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, SortOrder ordernarAscOuDesc,
			Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		itemsLazy = items.buscaPorPaginacao(posicaoPrimeiraLinha,
				maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			this.setRowCount(items.countAll(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return itemsLazy;
	}

	@Override
	public Object getRowKey(Item Item) {
		return Item.getCodigo();
	}

	@Override
	public Item getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Item item : itemsLazy) {
			if (id.equals(item.getCodigo())) {
				return item;
			}
		}

		return null;
	}

	@Override
	public void setRowIndex(int rowIndex) {
		// solução para evitar ArithmeticException
		if (rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else
			super.setRowIndex(rowIndex % getPageSize());
	}

	public List<Item> getItemsLazy() {
		return itemsLazy;
	}

	public Items getItems() {
		return items;
	}

}
