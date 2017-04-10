import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.crypto.Data;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.stat.Statistics;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.model.vo.DataQuantidade;

public class TestaGrafico {
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager em = factory.createEntityManager();
		Session session = em.unwrap(Session.class);
//		SessionFactory hbmSession = session.getSessionFactory();
//
//		Statistics statistics = hbmSession.getStatistics();
		Usuario encarregado = new Usuario();
		encarregado = em.find(Usuario.class, 2L);
		System.out.println("Encarregado :  " + encarregado.getNome());
		
		Empresa empresa = new Empresa();
		empresa = em.find(Empresa.class, 1L);
		
		System.out.println("dataIni Main ====== :  " + empresa.getDataIni()
				+ "  Nome  Empresa : " + empresa.getNomeFantasia());

		Map<Date, Long> valores = quantidadePorData( null,
				encarregado.getEmpresa(), session);

		for (Date data : valores.keySet()) {
			System.out.println("Data :" + data + "  Valores :"
					+ valores.get(data));
		}

		Map<Date, Long> valores2 = todosItems(encarregado.getEmpresa(), session);

		for (Date data : valores2.keySet()) {
			System.out.println("Data :" + data + "  Valores : "
					+ valores2.get(data));
		}

		
	}

	@SuppressWarnings({ "unchecked" })
	public static Map<Date, Long> quantidadePorData(Usuario encarregado,
			Empresa empresa, Session session) {

		Calendar dataInicial = Calendar.getInstance();

		System.out.println("-----dataIniEmpresa ----- :  " + empresa.getDataIni());
		dataInicial.setTime(empresa.getDataIni());

		System.out.println("-----Data Ini---- :  " + dataInicial);
		Calendar dataFinal = Calendar.getInstance();
		
		System.out.println("-----data Fim Empresa ----- :  " + empresa.getDataFim());
		dataFinal.setTime(empresa.getDataFim());
		System.out.println("----Data Final Calendar ---- :  " + dataFinal);

		int intervalo = 0;

		intervalo = dataFinal.get(Calendar.DAY_OF_YEAR)
				- dataInicial.get(Calendar.DAY_OF_YEAR);

		System.out.println("------Intervalo ------ :  " + intervalo);

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

		Criteria criteria = session.createCriteria(Item.class);

		criteria.setProjection(Projections
				.projectionList()
				.add(Projections.sqlGroupProjection("date(data_item) as data",
						"date(data_item)", new String[] { "data" },
						new Type[] { StandardBasicTypes.DATE }))

				.add(Projections.count("codigo").as("quantidade")));

		criteria.createAlias("tecnico", "t");
		criteria.createAlias("t.empresa", "e");
		criteria.createAlias("t.chefe", "c");
		if (encarregado != null) {
			criteria.add(Restrictions.eq("c.codigo", encarregado.getCodigo()));
			System.out.println("Encarregado :  " + encarregado.getNome());
		}

		List<DataQuantidade> quantidadesPorData = criteria
				.setResultTransformer(
						Transformers.aliasToBean(DataQuantidade.class)).list();
		for (DataQuantidade dataQuantidade : quantidadesPorData) {
			resultado.put(dataQuantidade.getData(),
					dataQuantidade.getQuantidade());

			System.out.println("Data Item Encarregado ======   -->" + dataQuantidade.getData()
					+ "   Quantidade Item Encarregado ======   -->" + dataQuantidade.getQuantidade());

		}

		return resultado;

	}

	@SuppressWarnings({ "unchecked" })
	public static Map<Date, Long> todosItems(
			Empresa empresa, Session session) {

		Calendar dataInicial = Calendar.getInstance();

		System.out.println("======dataIniEmpresa ====== :  " + empresa.getDataIni());
		dataInicial.setTime(empresa.getDataIni());

		System.out.println("====== DataIniCalendar ===== :  " + dataInicial);
		Calendar dataFinal = Calendar.getInstance();
		
		System.out.println("=======data Fim Empresa =======:  " + empresa.getDataFim());
		dataFinal.setTime(empresa.getDataFim());
		System.out.println("======= DataFinal calendar =======  :  " + dataFinal);

		int intervalo = 0;

		intervalo = dataFinal.get(Calendar.DAY_OF_YEAR)
				- dataInicial.get(Calendar.DAY_OF_YEAR);
	

		Map<Date, Long> resultado = criarMapaVazio(intervalo, dataInicial);

		/* Todos Items da Empresa */
		// select DATE(data_item) DATA ,count(i.codigo)
		// from item i
		// join empresa e on i.codigo_empresa = e.codigo
		// where codigo_empresa=1

		Criteria criteria = session.createCriteria(Item.class);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("dataItem"), "data")
				.add(Projections.count("codigo").as("quantidade")));

		criteria.createAlias("empresa", "e");

		criteria.add(Restrictions.eq("e.codigo", empresa.getCodigo()));
	

		List<DataQuantidade> quantidadesPorData = criteria
				.setResultTransformer(
						Transformers.aliasToBean(DataQuantidade.class)).list();
		
		for (DataQuantidade dataQuantidade : quantidadesPorData) {
			
			for (int i = 0; i <=10; i++) {
				resultado.put(dataInicial.getTime(), dataQuantidade.getQuantidade());
				dataInicial.add(Calendar.DATE, 1);
				
				System.out.println("Data Todos Items ======  -->" + dataQuantidade.getQuantidade()
						+ "   Quantidade Todos Items =======-->" + dataInicial);
			}
			//resultado.put(dataQuantidade.getData(),
			//		dataQuantidade.getQuantidade());

			System.out.println("Data Todos Items ======  -->" + dataQuantidade.getData()
					+ "   Quantidade Todos Items =======-->" + dataQuantidade.getQuantidade());
		}

		return resultado;

	}

	private static Map<Date, Long> criarMapaVazio(Integer numeroDeDias,
			Calendar dataInicial) { // Cria um mapa vazio para e não existir
									// informação o grafico de linhas, a linha
									// ir no zero.

		dataInicial = (Calendar) dataInicial.clone();

		Map<Date, Long> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= 10; i++) {
			mapaInicial.put(dataInicial.getTime(), 0L);
			dataInicial.add(Calendar.DATE, 1);
		}

		return mapaInicial;
	}

}
