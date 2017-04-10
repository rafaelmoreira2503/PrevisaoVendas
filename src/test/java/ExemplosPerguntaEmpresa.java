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

public class ExemplosPerguntaEmpresa {
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
	public void perguntasAssociadas() {
		//"select m from Item m where (select count(i) from Item i where i.codigoItem = m.codigoItem)=1
		Query query = manager
				.createQuery("select e from Empresa e where not exists (select distinct(pe.codigo_empresa) from PerguntaEmpresa pe where pe.codigo_empresa=e and pe.itemCriticado is not null and pe.areaCriticada is not null)");
						

		List<Empresa> areas = query.getResultList();
		System.out.println("---Impressão de Associadas----");

		for (Empresa area : areas)
			System.out.println(area.getCodigo()+" Empresa  : " + area.getNome());
	}
	
	
	
//	@Test
//	public void perguntasNaoAssociadas() {
//		//"select m from Item m where (select count(i) from Item i where i.codigoItem = m.codigoItem)=1
//		//Perguntas não Associadas
//		Query query = manager
//				.createQuery("select p from Pergunta p where not exists( "
//						+"from PerguntaEmpresa pe where pe.codigo_empresa.codigo=2L and pe.codigo_pergunta= p.codigo)");
//						
//
//		List<Pergunta> areas = query.getResultList();
//		System.out.println("---Impressão de Não Associadas----");
//
//		for (Pergunta area : areas)
//			System.out.println(area.getCodigo()+" Pergunta  : " + area.getDescricao());
//	}
//	
	
	
	
//	@Test
//	public void perguntasNaoAssociadas() {
//		//"select m from Item m where (select count(i) from Item i where i.codigoItem = m.codigoItem)=1
//		//Perguntas não Associadas
//		Query query = manager
//				.createQuery("select pe from PerguntaEmpresa pe where pe.codigo_empresa.codigo=3L and not exists( "
//						+"from Pergunta p  where  p.codigo = pe.codigo_pergunta)");
//						
//
//		List<PerguntaEmpresa> areas = query.getResultList();
//		System.out.println("---Impressão de Associadas----");
//
//		for (PerguntaEmpresa area : areas)
//			System.out.println(area.getCodigo()+" Pergunta  : " + area.getCodigo_pergunta().getDescricao());
//
//	}


}
