import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.confiabilidade.model.ItemAbc;

public class ExemplosEmItems {
	private static EntityManagerFactory factory;

	private EntityManager manager;

	@BeforeClass
	public static void init() {
		factory = Persistence.createEntityManagerFactory("ConfiabilidadePU");
	}

	@Before
	public void setUp() {
		this.manager = factory.createEntityManager();
	}

	@After
	public void tearDown() {
		this.manager.close();

	}

	// @Test
	// public void estatisticaTecnicos() {
	// "select m from Item m where (select count(i) from Item i where
	// i.codigoItem = m.codigoItem)=1

	// Session session = manager.unwrap(Session.class);
	// Criteria criteria = session.createCriteria(Item.class).setProjection(
	// Projections.rowCount());
	// criteria.add(Restrictions.eq("status", StatusItem.CRITICADO));
	// criteria.add(Restrictions.eq("empresa.codigo", 1L));
	// criteria.add(Restrictions.eq("tecnico.codigo", 3L));
	//
	// // eliminando resultados repetidos
	// criteria.setResultTransformer(Transformers.aliasToBean(Object[].class));
	//
	// List<Object[]> items = criteria.list();
	// int i = 0;
	// for (Object[] item : items) {
	//
	// System.out.println("---Impressão de Items----");
	//
	// // for (Long item : items)
	// System.out.println(++i + "Quantidade" + item[0] + " ITem  : "
	// + ((Item) item[1]).getTecnico().getNome());
	// }
	// }

	// @Test
	// public void estatisticaTecnicos2() {
	// Query query = manager
	//
	// .createQuery(
	// "select distinct(i) from Item i join fetch i.tecnico u where i.status='CRITICADO' and i.empresa.codigo=1L)",
	// Item.class);
	//
	// List<Item> items = query.getResultList();
	// System.out.println("---Impressão de Items----");
	// int i = 0;
	// for (Item item : items)
	// System.out.println(++i + " ITem  : " + item.getTecnico().getNome());
	// }

	// .createQuery("select new
	// br.com.confiabilidade.model.vo.EstatisticaTecnico(select m from Item m
	// left join m.tecnicos t
	// where (m.status ='CRITICADO' or m.status ='JUSTIFICADO') and
	// t.codigo=:codigo ,"
	// select d from Item d join d.tecnicos t where t.codigo=:codigo)",
	// EstatisticaTecnico.class).setParameter("codigo", 3L);

	// .createQuery("select new br.com.confiabilidade.model.vo.EstatisticaTecnico(m,d from (select m from Item m left join  m.tecnicos t "
	// +"where (m.status ='CRITICADO' or m.status ='JUSTIFICADO') and t.codigo=:codigo ) as m,"
	// +"(select d from Item d join d.tecnicos t where t.codigo=:codigo)) as d",
	// EstatisticaTecnico.class).setParameter("codigo", 3L);

	@Test
	public void estatisticaTecnicos3() {
		// "select m from Item m where (select count(i) from Item i where
		// i.codigoItem = m.codigoItem)=1

		Query query = manager.createQuery(
				"select i.codigoItem from Item i where i.empresa.codigo=1L",
				String.class);
		List<String> items = query.getResultList();
		System.out.println("---Impressão de Items----");
		int i = 0;
		for (String item : items)
			System.out.println(++i + " ITem  : " + "Codigo Item "
					+ item);

	}

	// @Test
	// public void estatisticaTecnicos2() {
	// Query query = manager
	// .createQuery(
	// "select count (i.codigoItem) from Item i where not exists ( select abc from ItemAbc where i.codigoItem=abc.codigoItemAbc and abc.empresa.codigo=1L) and i.empresa.codigo=1L group by i.codigoItem",
	// Long.class);
	// Long items = ((Number) query.getSingleResult()).longValue();
	// System.out.println("---Impressão de Items----");
	// int i = 0;
	// // for (Empresa item : items)
	// System.out.println("Quantidade" + items);
	// // System.out.println(" Item " + item);
	// }
	// }

	// @Test
	// public void estatisticaTecnicos() {
	// // "select m from Item m where (select count(i) from Item i where
	// // i.codigoItem = m.codigoItem)=1
	//
	// Query query = manager.createQuery(
	// "select min(i.criticidade), i from Item i where i.status='CRITICADO'  "
	// + "and i.empresa.codigo=1L group by i.codigoItem) ",
	// Object[].class);
	// List<Object[]> items = query.getResultList();
	// System.out.println("---Impressão de Items----");
	//
	// for (Object[] item : items) {
	// int total = 0;
	// while (total < items.size()) {
	// System.out.print("value of x : " + total);
	// total++;
	// System.out.print("\n");
	// }
	//
	// for (int i = 1; i > items.size(); i++) {
	//
	// System.out.println("Quantidade " + ++total + "Size"
	// + items.size());
	// }
	// System.out.println("Quantidade " + total + "Size" + items.size());
	// System.out.println("Quantidade" + total + " Codogo Item"
	// + ((Item) item[1]).getCriticidade());
	// }
	// }

	// @Test
	// public void estatisticaTecnicos2() {
	// // "select m from Item m where (select count(i) from Item i where
	// // i.codigoItem = m.codigoItem)=1
	//
	// Long itemsdoGerente = manager
	// .createQuery(
	// "select count(i) as Quantidade from Item i where"
	// +
	// " (i.status='CRITICADO'or i.status<>'PENDENTE') and i.empresa.codigo=1L "
	// + "and i.tecnico.chefe.codigo=2L",
	// Long.class)
	//
	//
	// .getSingleResult();
	//
	// System.out.println( "Total ITem  : " +itemsdoGerente);
	//
	// }
	//

}
