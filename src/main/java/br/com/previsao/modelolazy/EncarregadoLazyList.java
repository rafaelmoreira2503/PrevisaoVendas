package br.com.previsao.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Usuario;
import br.com.previsao.repository.Usuarios;

public class EncarregadoLazyList extends LazyDataModel<Usuario> implements
		SelectableDataModel<Usuario>, Serializable {

	private static final long serialVersionUID = 1L;

	// Data Source for binding data to the DataTable
	private List<Usuario> encarregadosLazy;

	private Usuarios encarregados;
	private Empresa filtro;

	public EncarregadoLazyList(Usuarios encarregados, Empresa filtro) {
		this.encarregados = encarregados;
		this.filtro = filtro;
	}

	@Override
	public List<Usuario> load(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, SortOrder ordernarAscOuDesc,
			Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		encarregadosLazy = encarregados.buscaPorPaginacaoEncarregado(
				posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo,
				ordernacao, filtro);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(encarregados.countAllEncarregado(filtro));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return encarregadosLazy;
	}

	@Override
	public Object getRowKey(Usuario encarregado) {
		return encarregado.getCodigo();
	}

	@Override
	public Usuario getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Usuario encarregado : encarregadosLazy) {
			if (id.equals(encarregado.getCodigo())) {
				return encarregado;
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

	public List<Usuario> getEncarregadosLazy() {
		return encarregadosLazy;
	}

	public Usuarios getEncarregados() {
		return encarregados;
	}

}
