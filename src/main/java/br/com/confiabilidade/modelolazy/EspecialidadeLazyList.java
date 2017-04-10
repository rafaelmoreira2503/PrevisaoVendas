package br.com.confiabilidade.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.repository.Especialidades;

@Named
@ViewScoped
public class EspecialidadeLazyList extends LazyDataModel<Especialidade>
		implements Serializable {

	private static final long serialVersionUID = 1L;
	// Data Source for binding data to the DataTable
	private List<Especialidade> especialidadesLazy;

	private int rowCount;

	private Especialidades especialidades;

	public EspecialidadeLazyList(Especialidades especialidades) {
		this.especialidades = especialidades;
	}

	@Override
	public List<Especialidade> load(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo,
			SortOrder ordernarAscOuDesc, Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		especialidadesLazy = especialidades.buscaPorPaginacao(
				posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo,
				ordernacao, filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(especialidades.countAll(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return especialidadesLazy;
	}

	@Override
	public Object getRowKey(Especialidade Especialidade) {
		return Especialidade.getCodigo();
	}

	@Override
	public Especialidade getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Especialidade especialidade : especialidadesLazy) {
			if (id.equals(especialidade.getCodigo())) {
				return especialidade;
			}
		}

		return null;
	}

	@Override
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	@Override
	public int getRowCount() {
		return this.rowCount;
	}

	public List<Especialidade> getEspecialidadesLazy() {
		return especialidadesLazy;
	}

	public Especialidades getEspecialidades() {
		return especialidades;
	}

}
