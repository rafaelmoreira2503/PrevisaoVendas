import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.confiabilidade.model.Usuario;

public class ExemplosEmUsuarios {

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

	@Test
	public void estatisticaTecnicos() {
		// "select m from Item m where (select count(i) from Item i where
		// i.codigoItem = m.codigoItem)=1

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.setFetchMode("Subordinados", FetchMode.JOIN);
		criteria.add(Restrictions.isNull("chefe"));

		criteria.add(Restrictions.eq("empresa.codigo", 1L));

		

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		
			 
		
		List<Usuario> usuarios = criteria.list();
		int i = 0;
		for (Usuario usuario : usuarios) {

			System.out.println("---Impressão de Items----");

			// for (Long item : items)
			System.out.println(++i + " ITem  : " + usuario.getSubordinados().get(0).getNome());
		}

	}
}