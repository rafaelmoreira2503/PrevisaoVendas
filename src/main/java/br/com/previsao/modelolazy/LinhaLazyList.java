package br.com.previsao.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.previsao.model.Linha;
import br.com.previsao.repository.Linhas;

public class LinhaLazyList extends LazyDataModel<Linha> implements Serializable,
		SelectableDataModel<Linha> {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<Linha> linhasLazy;

	private int rowCount;
	// Data Access Service for create read update delete operations

	private Linhas linhas;

	public LinhaLazyList(Linhas linhas) {
		this.linhas = linhas;
		
	}

	public List<Linha> load(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, SortOrder ordernarAscOuDesc,
			Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		linhasLazy = linhas.buscaPorPaginacao(posicaoPrimeiraLinha,
				maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(linhas.countAll(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return linhasLazy;
	}

	@Override
	public Object getRowKey(Linha linha) {
		return linha.getCodigo();
	}

	@Override
	public Linha getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Linha linha : linhasLazy) {
			if (id.equals(linha.getCodigo())) {
				return linha;
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

	public List<Linha> getLinhasLazy() {
		return linhasLazy;
	}

	public Linhas getLinhas() {
		return linhas;
	}

}
