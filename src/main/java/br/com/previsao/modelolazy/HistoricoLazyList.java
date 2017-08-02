package br.com.previsao.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.previsao.model.Historico;
import br.com.previsao.repository.Historicos;

public class HistoricoLazyList extends LazyDataModel<Historico> implements Serializable,
		SelectableDataModel<Historico> {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<Historico> historicosLazy;

	private int rowCount;
	// Data Access Service for create read update delete operations

	
	
	private Historicos historicos;

	public HistoricoLazyList(Historicos historicos) {
		this.historicos = historicos;
		
	}

	public List<Historico> load(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, SortOrder ordernarAscOuDesc,
			Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		historicosLazy = historicos.buscaPorPaginacao(posicaoPrimeiraLinha,
				maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(historicos.countAll(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return historicosLazy;
	}

	@Override
	public Object getRowKey(Historico area) {
		return area.getCodigo();
	}

	@Override
	public Historico getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Historico hist : historicosLazy) {
			if (id.equals(hist.getCodigo())) {
				return hist;
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

	public List<Historico> getHistoricosLazy() {
		return historicosLazy;
	}

	public Historicos getHistoricos() {
		return historicos;
	}

}
