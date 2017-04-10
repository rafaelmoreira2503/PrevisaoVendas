package br.com.confiabilidade.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.ItemAbc;
import br.com.confiabilidade.repository.ItemsAbc;

public class AbcLazyItems extends LazyDataModel<ItemAbc> implements
		SelectableDataModel<ItemAbc>, Serializable {

	private static final long serialVersionUID = 1L;

	// Data Source for binding data to the DataTable
	private List<ItemAbc> itemsAbcLazy;

	private ItemsAbc itemsAbc;

	public AbcLazyItems(ItemsAbc itemsAbc) {
		this.itemsAbc = itemsAbc;

	}

	public List<ItemAbc> load(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, SortOrder ordernarAscOuDesc,
			Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		itemsAbcLazy = itemsAbc.buscaPorPaginacaoItemsAbc(posicaoPrimeiraLinha,
				maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			this.setRowCount(itemsAbc.countAllItemsAbc(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return itemsAbcLazy;
	}

	@Override
	public Object getRowKey(ItemAbc item) {
		return item.getCodigo();
	}

	@Override
	public ItemAbc getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (ItemAbc item : itemsAbcLazy) {
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

	public List<ItemAbc> getItemsAbcLazy() {
		return itemsAbcLazy;
	}

	public ItemsAbc getItemsAbc() {
		return itemsAbc;
	}

}
