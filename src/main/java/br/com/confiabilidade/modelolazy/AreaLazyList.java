package br.com.confiabilidade.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.repository.Areas;

public class AreaLazyList extends LazyDataModel<Area> implements Serializable,
		SelectableDataModel<Area> {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<Area> areasLazy;

	private int rowCount;
	// Data Access Service for create read update delete operations

	
	
	private Areas areas;

	public AreaLazyList(Areas areas) {
		this.areas = areas;
		
	}

	public List<Area> load(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, SortOrder ordernarAscOuDesc,
			Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		areasLazy = areas.buscaPorPaginacao(posicaoPrimeiraLinha,
				maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(areas.countAll(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return areasLazy;
	}

	@Override
	public Object getRowKey(Area area) {
		return area.getCodigo();
	}

	@Override
	public Area getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Area area : areasLazy) {
			if (id.equals(area.getCodigo())) {
				return area;
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

	@Override
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	@Override
	public int getRowCount() {
		return this.rowCount;
	}

	public List<Area> getAreasLazy() {
		return areasLazy;
	}

	public Areas getAreas() {
		return areas;
	}

}
