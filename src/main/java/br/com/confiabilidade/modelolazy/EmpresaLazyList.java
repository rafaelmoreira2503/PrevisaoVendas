package br.com.confiabilidade.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.repository.Empresas;

public class EmpresaLazyList extends LazyDataModel<Empresa> implements
		Serializable {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<Empresa> empresasLazy;

	private Empresas empresas;

	public EmpresaLazyList(Empresas empresas) {
		this.empresas = empresas;
	}

	@Override
	public List<Empresa> load(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, SortOrder ordernarAscOuDesc,
			Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		empresasLazy = empresas.buscaPorPaginacao(posicaoPrimeiraLinha,
				maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(empresas.countAll(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return empresasLazy;
	}

	@Override
	public Object getRowKey(Empresa Empresa) {
		return Empresa.getCodigo();
	}

	@Override
	public Empresa getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Empresa empresa : empresasLazy) {
			if (id.equals(empresa.getCodigo())) {
				return empresa;
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

	public List<Empresa> getEmpresasLazy() {
		return empresasLazy;
	}

	public Empresas getEmpresas() {
		return empresas;
	}

}
