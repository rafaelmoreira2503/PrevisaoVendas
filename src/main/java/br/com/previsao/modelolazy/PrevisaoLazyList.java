package br.com.previsao.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.previsao.model.Linha;
import br.com.previsao.model.Produto;
import br.com.previsao.repository.Produtos;

public class PrevisaoLazyList extends LazyDataModel<Produto> implements Serializable, SelectableDataModel<Produto> {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<Produto> previsaoLazy;

	private int rowCount;
	// Data Access Service for create read update delete operations

	private Produtos previsoes;
	private Linha filtro;

	public PrevisaoLazyList(Produtos previsoes, Linha filtro) {
		this.previsoes = previsoes;
		this.filtro = filtro;

	}

	public List<Produto> load(int posicaoPrimeiraLinha, int maximoPorPagina, String ordernarPeloCampo,
			SortOrder ordernarAscOuDesc, Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		previsaoLazy = previsoes.buscaPorPaginacaoPrevisao(posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo,
				ordernacao, filtro);
		
		for(Produto prod : previsaoLazy){
			System.out.println(prod.getDescricao());
		}
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(previsoes.countAllPrevisao(filtro));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return previsaoLazy;
	}

	@Override
	public Object getRowKey(Produto area) {
		return area.getCodigo();
	}

	@Override
	public Produto getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Produto prod : previsaoLazy) {
			if (id.equals(prod.getCodigo())) {
				return prod;
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

	public List<Produto> getPrevisaoLazy() {
		return previsaoLazy;
	}

	public Produtos getProdutos() {
		return previsoes;
	}

}
