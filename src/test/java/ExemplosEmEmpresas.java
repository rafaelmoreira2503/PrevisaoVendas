import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.confiabilidade.model.Empresa;

public class ExemplosEmEmpresas {
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
	public void empresascomCritica() {
		/*Existe Empresa para pergunta e item CRITICADOOO!!!!
		 * 
		 * 
		 */
		Query query = manager
				.createQuery("select e from Empresa e where not exists(select i from Item i where i.empresa=e ) ");

		List<Empresa> empresas = query.getResultList();
		System.out.println("---Impressão de Associadas----");

		for (Empresa empresa : empresas)
			System.out.println(empresa.getCodigo() + " Empresa  : "
					+ empresa.getNomeFantasia());
	}

//	@Test
//	public void empresasParaImportacao() {
//		// "select m from Item m where (select count(i) from Item i where
//		// i.codigoItem = m.codigoItem)=1
//		// Empresas não Associadas
//		Query query = manager
//				.createQuery("select e from Empresa e where e not in (select i.empresa from Item i)");
//
//		List<Empresa> empresas = query.getResultList();
//		System.out.println("---Impressão de Não Associadas----");
//
//		for (Empresa empresa : empresas)
//			System.out.println(empresa.getCodigo() + " Empresa  : "
//					+ empresa.getNome());
//	}

}
