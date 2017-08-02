package br.com.previsao.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.previsao.model.Familia;
import br.com.previsao.repository.Familias;

public class FamiliaLazyList extends LazyDataModel<Familia> implements Serializable,SelectableDataModel<Familia> {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<Familia> familiasLazy;

	private int rowCount;
	// Data Access Service for create read update delete operations

	private Familias familias;

	public FamiliaLazyList(Familias familias) {
		this.familias = familias;

	}

	public List<Familia> load(int posicaoPrimeiraLinha, int maximoPorPagina, String ordernarPeloCampo,
			SortOrder ordernarAscOuDesc, Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		familiasLazy = familias.buscaPorPaginacao(posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo, ordernacao,
				filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(familias.countAll(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return familiasLazy;
	}

	@Override
	public Object getRowKey(Familia area) {
		return area.getCodigo();
	}

	@Override
	public Familia getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Familia area : familiasLazy) {
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

	public List<Familia> getFamiliasLazy() {
		return familiasLazy;
	}

	public Familias getFamilias() {
		return familias;
	}

}
