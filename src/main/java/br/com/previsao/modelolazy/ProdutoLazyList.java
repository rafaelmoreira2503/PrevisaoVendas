package br.com.previsao.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Produto;
import br.com.previsao.repository.Produtos;

public class ProdutoLazyList extends LazyDataModel<Produto> implements Serializable, SelectableDataModel<Produto> {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<Produto> produtosLazy;

	private int rowCount;
	// Data Access Service for create read update delete operations

	private Produtos produtos;
	
	private Empresa filtro;

	public ProdutoLazyList(Produtos produtos, Empresa filtro) {
		this.produtos = produtos;
		this.filtro = filtro;

	}

	public List<Produto> load(int posicaoPrimeiraLinha, int maximoPorPagina, String ordernarPeloCampo,
			SortOrder ordernarAscOuDesc, Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		produtosLazy = produtos.buscaPorPaginacao(posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo, ordernacao,
				filtro);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(produtos.countAll(filtro));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return produtosLazy;
	}

	@Override
	public Object getRowKey(Produto area) {
		return area.getCodigo();
	}

	@Override
	public Produto getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Produto area : produtosLazy) {
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

	public List<Produto> getProdutosLazy() {
		return produtosLazy;
	}

	public Produtos getProdutos() {
		return produtos;
	}

}
