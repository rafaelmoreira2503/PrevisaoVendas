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

public class TecnicoLazyList extends LazyDataModel<Usuario> implements
		SelectableDataModel<Usuario>, Serializable {
	private static final long serialVersionUID = 1L;

	// Data Source for binding data to the DataTable
	private List<Usuario> tecnicosLazy;

	private Usuarios tecnicos;

	private Empresa filtro;

	public TecnicoLazyList(Usuarios tecnicos, Empresa filtro) {
		this.tecnicos = tecnicos;
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
		tecnicosLazy = tecnicos.buscaPorPaginacaoTecnico(posicaoPrimeiraLinha,
				maximoPorPagina, ordernarPeloCampo, ordernacao, filtro);

		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente

		setRowCount(tecnicos.countAllTecnico(filtro));

		// quantidade a ser exibida em cada página
		setPageSize(maximoPorPagina);

		return tecnicosLazy;
	}

	@Override
	public Object getRowKey(Usuario tecnico) {
		return tecnico.getCodigo();
	}

	@Override
	public Usuario getRowData(String tecnicoId) {
		Integer id = Integer.valueOf(tecnicoId);

		for (Usuario tecnico : tecnicosLazy) {
			if (id.equals(tecnico.getCodigo())) {
				return tecnico;
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

	public List<Usuario> getTecnicosLazy() {
		return tecnicosLazy;
	}

	public Usuarios getTecnicos() {
		return tecnicos;
	}

}
