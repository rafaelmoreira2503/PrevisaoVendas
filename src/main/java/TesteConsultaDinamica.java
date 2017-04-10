import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.model.vo.DataQuantidade;

public class TesteConsultaDinamica {

	public static void main(String[] args) {

		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager manager = factory.createEntityManager();

		Usuario encarregado = new Usuario();
		encarregado = manager.find(Usuario.class, 2L);
		System.out.println("Encarregado :  " + encarregado.getNome());
		Empresa empresa = new Empresa();
		empresa = manager.find(Empresa.class, 1L);
		System.out.println("dataIni :  " + empresa.getDataIni() + "Nome"
				+ empresa.getNomeFantasia());

		Map<Date, Long> valores = quantidadePorData(empresa, encarregado,
				manager);

		for (Date data : valores.keySet()) {
			System.out.println("Data :" + data + "Valores :"
					+ valores.get(data));
		}
	}

	// // so os items que são dos tecnicos nao tem left join
	// String jpql =
	// "select distinct(i) from Item i join fetch i.empresa e left join fetch i.tecnicos u  where e.codigo=1 order by i.codigo ";
	//
	// // Items dostecnicos feitos para ter todos referentes a ele trocar por
	// // PENDENTE
	//
	// List<DataQuantidade> quantidadesPorData = manager
	// .createQuery(
	// "select new br.com.confiabilidade.model.vo.DataQuantidade(i.dataItem,count(i)) from Item i  ",
	// DataQuantidade.class).getResultList();
	//
	//
	//
	// int count = 0;
	// for (DataQuantidade dataQuantidade : quantidadesPorData) {
	//
	// System.out.println(++count + ":" + dataQuantidade.getData() + "---"
	// + dataQuantidade.getQuantidade());
	// }

	// Items dos tecnicos feitos para ter todos referentes a ele trocar por
	// PENDENTE

	public static Map<Date, Long> quantidadePorData(Empresa empresa,
			Usuario encarregado, EntityManager manager) {

		Calendar dataInicial = Calendar.getInstance();

		System.out.println("dataIni :  " + empresa.getDataIni());
		dataInicial.setTime(empresa.getDataIni());
		System.out.println("Data Utils :" + dataInicial);

		// dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		// // trunca
		// a
		// data
		// do
		// dia
		// do
		// mes
		// sem
		// hora
		// e
		// minuto.

		Calendar dataFinal = Calendar.getInstance();
		dataFinal.setTime(empresa.getDataFim());
		// dataFinal = DateUtils.truncate(dataFinal, Calendar.DAY_OF_MONTH);

		// dataInicial = (Calendar) dataInicial.clone();
		//
		// dataFinal = (Calendar) dataFinal.clone();

		int intervalo = 0;

		intervalo = dataFinal.get(Calendar.DAY_OF_YEAR)
				- dataInicial.get(Calendar.DAY_OF_YEAR);
		System.out.println("O Intervalo de Datas:  "
				+ dataFinal.get(Calendar.DAY_OF_YEAR) + "e "
				+ dataInicial.get(Calendar.DAY_OF_YEAR));

		System.out.println("O Intervalo de Datas: Final--> "
				+ dataFinal.getTime() + "e inicial-->" + dataInicial.getTime());
		System.out.println("Intervalo :  " + intervalo);

		// for (int i = 0; i < intervalo; i++) {
		// dataInicial.add(Calendar.DATE, 1);
		//
		// System.out.println(i + " - " + dataInicial.get(Calendar.DATE) + "/"
		// + (dataInicial.get(Calendar.MONTH) + 1) + "/"
		// + dataInicial.get(Calendar.YEAR));
		// }

		Map<Date, Long> resultado = criarMapaVazio(intervalo, dataInicial);

		/* Grafico do Encarregado */
		// select DATE(data_item) DATA ,count(i.codigo)
		// from item i
		// join tecnico_item ti on (ti.codigo_item = i.codigo)
		// join usuario tecnico on (tecnico.codigo = ti.codigo_tecnico)
		// join usuario chefe on (chefe.codigo = tecnico.codigo_chefe)
		// where chefe.codigo = 2
		// group by
		// data_item;

		List<DataQuantidade> itemsdoEncarregado = manager
				.createQuery(
						"select new br.com.confiabilidade.model.vo.DataQuantidade(i.dataItem as data , count(i) as quantidade) from Item i join i.tecnicos u join u.chefe c where c.codigo=:codigo group by i.dataItem",
						DataQuantidade.class)
				.setParameter("codigo", encarregado.getCodigo())
				.getResultList();

		for (DataQuantidade items : itemsdoEncarregado) {
			System.out.println("Data " + items.getData()
					+ "  --->  Items feitos : " + items.getQuantidade());
		}
		return resultado;
	}

	private static Map<Date, Long> criarMapaVazio(Integer numeroDeDias,
			Calendar dataInicial) { // Cria um mapa vazio para e não existir
									// informação o grafico de linhas, a linha
									// ir no zero.

		dataInicial = (Calendar) dataInicial.clone();
		System.out.println("DataInicial que Quero ver:   "
				+ dataInicial.getTime());
		System.out.println("Número de Dias : " + numeroDeDias);
		Map<Date, Long> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			System.out.println(i + " - " + dataInicial.get(Calendar.DATE) + "/"
					+ (dataInicial.get(Calendar.MONTH) + 1) + "/"
					+ dataInicial.get(Calendar.YEAR));
			mapaInicial.put(dataInicial.getTime(), 0L);
			dataInicial.add(Calendar.DATE, 1);
		}

		return mapaInicial;
	}

}
