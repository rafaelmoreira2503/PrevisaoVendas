package br.com.confiabilidade.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.ItemAbc;
import br.com.confiabilidade.model.TempoDeReposicao;
import br.com.confiabilidade.repository.TempoDeReposicoes;

public class TempoReposicaoLazyList extends LazyDataModel<TempoDeReposicao>
		implements Serializable, SelectableDataModel<TempoDeReposicao> {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<TempoDeReposicao> TempoDeReposicoesLazy;

	private int rowCount;
	// Data Access Service for create read update delete operations

	private TempoDeReposicoes TempoDeReposicoes;

	private ItemAbc itemAbc;

	public TempoReposicaoLazyList(TempoDeReposicoes TempoDeReposicoes,
			ItemAbc itemAbc) {
		this.TempoDeReposicoes = TempoDeReposicoes;
		this.itemAbc = itemAbc;

	}

	public List<TempoDeReposicao> load(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo,
			SortOrder ordernarAscOuDesc, Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		TempoDeReposicoesLazy = TempoDeReposicoes
				.buscaPorPaginacaoTempoDeReposicao(posicaoPrimeiraLinha,
						maximoPorPagina, ordernarPeloCampo, ordernacao, itemAbc);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(TempoDeReposicoes.countAllTempoDeReposiucoes(itemAbc));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return TempoDeReposicoesLazy;
	}

	@Override
	public Object getRowKey(TempoDeReposicao TempoDeReposicao) {
		return TempoDeReposicao.getCodigo();
	}

	@Override
	public TempoDeReposicao getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (TempoDeReposicao TempoDeReposicao : TempoDeReposicoesLazy) {
			if (id.equals(TempoDeReposicao.getCodigo())) {
				return TempoDeReposicao;
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

	public List<TempoDeReposicao> getTempoDeReposicoesLazy() {
		return TempoDeReposicoesLazy;
	}

	public TempoDeReposicoes getTempoDeReposicoes() {
		return TempoDeReposicoes;
	}

}
