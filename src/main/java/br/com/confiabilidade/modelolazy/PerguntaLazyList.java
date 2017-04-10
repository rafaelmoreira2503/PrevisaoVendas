package br.com.confiabilidade.modelolazy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.repository.Perguntas;

public class PerguntaLazyList extends LazyDataModel<Pergunta> implements
		Serializable {
	private static final long serialVersionUID = 1L;

	// Data Source for binding data to the DataTable
	private List<Pergunta> perguntasLazy;

	private Perguntas perguntas;

	public PerguntaLazyList(Perguntas perguntas) {
		this.perguntas = perguntas;
	}

	@Override
	public List<Pergunta> load(int posicaoPrimeiraLinha, int maximoPorPagina,
			String ordernarPeloCampo, SortOrder ordernarAscOuDesc,
			Map<String, String> filtros) {

		String ordernacao = ordernarAscOuDesc.toString();

		if (SortOrder.UNSORTED.equals(ordernarAscOuDesc)) {
			ordernacao = SortOrder.ASCENDING.toString();
		}

		// with datatable pagination limits
		perguntasLazy = perguntas.buscaPorPaginacao(posicaoPrimeiraLinha,
				maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
		// total encontrado no banco de dados, caso o filtro esteja preenchido
		// dispara a consulta novamente
		if (getRowCount() <= 0 || (filtros != null && !filtros.isEmpty())) {
			setRowCount(perguntas.countAll(filtros));
		}

		// quantidade a ser exibida em cada pÃ¡gina
		setPageSize(maximoPorPagina);

		return perguntasLazy;
	}

	@Override
	public Object getRowKey(Pergunta Pergunta) {
		return Pergunta.getCodigo();
	}

	@Override
	public Pergunta getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);

		for (Pergunta pergunta : perguntasLazy) {
			if (id.equals(pergunta.getCodigo())) {
				return pergunta;
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

	public List<Pergunta> getPerguntasLazy() {
		return perguntasLazy;
	}

	public Perguntas getPerguntas() {
		return perguntas;
	}

}
