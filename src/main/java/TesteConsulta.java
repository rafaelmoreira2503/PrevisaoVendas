import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Usuario;

public class TesteConsulta {
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager em = factory.createEntityManager();
		Session session = em.unwrap(Session.class);

		Usuario tecnico = new Usuario();
		tecnico.setCodigo(5L);
		List<Usuario> tecnicos = new ArrayList<Usuario>();

		tecnicos.add(tecnico);
		Map<Date, Long> valores = quantidadePorData(70, tecnicos, session);

		for (Date data : valores.keySet()) {
			System.out.println("Data :" + data + "Valores :"
					+ valores.get(data));
		}

		// JFrame frame = new JFrame("Charts");

		// frame.setSize(600, 400);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setVisible(true);
		// DefaultXYDataset ds = new DefaultXYDataset();
		// ds.("series1", valores);
		// XYDataset ds = createDataset();
		// JFreeChart chart = ChartFactory.createTimeSeriesChart("eee", "Tempo",
		// "X", valores, false, true, false);
		// ("Test Chart","x", "y", valores, PlotOrientation.VERTICAL, false,
		// true,false);

		// ChartPanel cp = new ChartPanel(chart);

		// frame.getContentPane().add(cp);

	}

	@SuppressWarnings({ "unchecked" })
	public static Map<Date, Long> quantidadePorData(Integer numeroDeDias,
			List<Usuario> criadoPor, Session session) {

		numeroDeDias -= 1;

		Calendar dataInicial = Calendar.getInstance();

		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

		Calendar dataFinal = Calendar.getInstance();
		dataFinal = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataFinal.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

		Map<Date, Long> resultado = criarMapaVazio(numeroDeDias, dataInicial);

		// select date(data_criacao) as data, sum(valor_total) as valor
		// from pedido where data_criacao >= :dataInicial and vendedor_id =
		// :criadoPor
		// group by date(data_criacao)

		Criteria criteria = session.createCriteria(Item.class);

		// select date(data_criacao) as data, sum(valor_total) as valor
		// from pedido where data_criacao >= :dataInicial and vendedor_id =
		// :criadoPor
		// group by date(data_criacao)

		criteria.setProjection(
				Projections
						.projectionList()
						.add(Projections.sqlGroupProjection(
								"date(data_item) as data", "date(data_item)",
								new String[] { "data" },
								new Type[] { StandardBasicTypes.DATE }))

						.add(Projections.count("codigo").as("quantidade")))

		.add(Restrictions.ge("dataItem", dataInicial.getTime()));

		if (criadoPor != null) {
			criteria.createAlias("tecnico", "tecnico");
			criteria.add(Restrictions.eq("tecnico.codigo", 2L));
		}
		List<Object[]> quantidadesPorData = criteria.setResultTransformer(
				Transformers.aliasToBean(Object[].class)).list();

		for (Object[] dataQuantidade : quantidadesPorData) {
			resultado.put((Date) dataQuantidade[0], ((Long) dataQuantidade[1]));
		}

		return resultado;

	}

	private static Map<Date, Long> criarMapaVazio(Integer numeroDeDias,
			Calendar dataInicial) { // Cria um mapa vazio para e não existir
									// informação o grafico de linhas, a linha
									// ir no zero.

		dataInicial = (Calendar) dataInicial.clone();

		Map<Date, Long> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			mapaInicial.put(dataInicial.getTime(), 0L);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mapaInicial;
	}

}
