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

public class GerenteLazyList extends LazyDataModel<Usuario> implements
		SelectableDataModel<Usuario>, Serializable {

	private static final long serialVersionUID = 1L;

	// Data Source for binding data to the DataTable
	private List<Usuario> gerentesLazy;

	private Usuarios gerentes;
	private Empresa filtro;

	public GerenteLazyList(Usuarios gerentes, Empresa filtro) {
		this.gerentes = gerentes;
		this.filtro = filtro;
	}

	@Override
	public List<Usuario> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {
		String ordernacao = sortOrder.toString();

		if (SortOrder.UNSORTED.equals(sortOrder)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		gerentesLazy = gerentes.buscaPorPaginacaoGerente( first,  pageSize,
				 sortField, ordernacao,  filtro);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente

		setRowCount(gerentes.countAllGerente(filtro));

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(pageSize);

		return gerentesLazy;
	}

	@Override
	public Object getRowKey(Usuario gerente) {
		return gerente.getCodigo();
	}

	@Override
	public Usuario getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Usuario gerente : gerentesLazy) {
			if (id.equals(gerente.getCodigo())) {
				return gerente;
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

	public List<Usuario> getGerentesLazy() {
		return gerentesLazy;
	}

	public Usuarios getGerentes() {
		return gerentes;
	}

}
