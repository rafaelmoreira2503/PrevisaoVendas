import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.com.confiabilidade.model.Usuario;

public class TestaFetchJoin {

	public static void main(String[] args) {

		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("ConfiabilidadePU");
			EntityManager manager = factory.createEntityManager();

			EntityTransaction trx = manager.getTransaction();
			trx.begin();

			/*
			 * JOIN FETCH TRAZ O MESMO RESULTADO QUE INNER JOIN FETCH E JOIN
			 * TRAZ SOMENTE OS QUE TEM RELACIONAMENTO.
			 * 
			 * (JOIN/JOIN FETCH/INNER JOIN FETCH) SÃO IGUAIS PARA
			 * RELACIONAMENTOS NA MESMA TABELA!!!!!
			 * 
			 * LEFT JOIN FETCH TRAZ TODOS , INCLUSIVE OS QUE NÃO TEM
			 * RELACIONAMENTO.
			 * 
			 * Usuario encarregado = manager.find(Usuario.class, 1L);
			 * 
			 * Query that will fetch a lazy relationship Be carefull, with this
			 * kind of query only those who have the relationship will come in
			 * the result
			 * 
			 * private static Person findPersonByNameWithAllDogs(EntityManager
			 * em, String name) { Query query = em.createQuery(
			 * "select p from Person p join fetch p.dogs where p.name = :name",
			 * Person.class); query.setParameter("name", name); return (Person)
			 * query.getSingleResult(); }
			 * 
			 * 
			 * With this query will will bring results that may not have
			 * arelationship
			 * 
			 * private static Person
			 * findPersonByNameThatMayNotHaveDogs(EntityManager em, String name)
			 * { Query query = em.createQuery(
			 * "select p from Person p left join fetch p.dogs where p.name = :name"
			 * , Person.class); query.setParameter("name", name); return
			 * (Person) query.getSingleResult(); }
			 * 
			 * 
			 * 
			 * WHERE Filter in Multi Variable Queries
			 * 
			 * In a multi-variable query the FROM clause defines iteration on
			 * tuples. In this case the WHERE clause filters tuples before
			 * passing them to the SELECT clause. For example, the following
			 * query retrieves all the countries with population size that
			 * exceeds a specified limit and also have an official language from
			 * a specified set of languages:
			 * 
			 * SELECT c, l FROM Country c JOIN c.languages l WHERE c.population
			 * > :p AND l IN :languages
			 */

			// Todos os Gerentes da Empresa
			String jpql = "select u from Usuario u join fetch u.empresa where u.chefe is null";
			TypedQuery<Usuario> query3 = manager.createQuery(jpql,
					Usuario.class);
			List<Usuario> gerentes = query3.getResultList();
			for (Usuario usuGerente : gerentes) {
				System.out.println(" Impressão de Gerentes da Empresa: ");
				System.out.println(" Nome : " + usuGerente.getCodigo());
				System.out.println(" Nome : " + usuGerente.getNome());
			}

			// Quantidade de Gerentes
			String q2 = "select COUNT(u) from Empresa e JOIN e.usuarios u where u.chefe is null";
			TypedQuery<Long> query1 = manager.createQuery(q2, Long.class);
			Integer quantidadeDeGerentes = query1.getSingleResult().intValue();
			System.out.println(" Impressão de Quantidade de Gerentes : ");
			System.out.println(" Tem  : " + quantidadeDeGerentes);

			// Todos os Encaregados da Empresa
			String encarregado = "select u from Usuario u join fetch u.empresa where u.permissao='ENCARREGADO'";
			TypedQuery<Usuario> query4 = manager.createQuery(encarregado,
					Usuario.class);
			List<Usuario> encatrregados = query4.getResultList();
			for (Usuario usuEncarregado : encatrregados) {
				System.out.println(" Impressão de Encarregados da Empresa: ");
				System.out.println(" Nome : " + usuEncarregado.getCodigo());
				System.out.println(" Nome : " + usuEncarregado.getNome());
				System.out.println(" Nome : " + usuEncarregado.getAreaEncarregado());

			}
			// Quantidade de Encarregados
			String q3 = "select COUNT(u) from Empresa e JOIN e.usuarios u where u.permissao='ENCARREGADO'";
			TypedQuery<Long> query5 = manager.createQuery(q3, Long.class);
			Integer quantidadeDeEncarregados = query5.getSingleResult()
					.intValue();
			System.out.println(" Impressão de Quantidade de Encarregados : ");
			System.out.println(" Tem  : " + quantidadeDeEncarregados);

			// Todos os Técnicos da Empresa
			String jpql0 = "select u from Usuario u join fetch u.empresa join fetch u.chefe c join fetch c.chefe";
			TypedQuery<Usuario> query2 = manager.createQuery(jpql0,
					Usuario.class);
			List<Usuario> usuarios = query2.getResultList();
			for (Usuario usuTecnico : usuarios) {
				System.out.println(" Impressão de Técnicos da Empresa: ");
				System.out.println(" Nome : " + usuTecnico.getCodigo());
				System.out.println(" Nome : " + usuTecnico.getNome());

			}

			// Quantos Técnicos tem a Empresa
			String q = "select COUNT(s) from Empresa e JOIN e.usuarios u JOIN u.subordinados s JOIN s.subordinados";
			TypedQuery<Long> query = manager.createQuery(q, Long.class);
			Integer quantidadeDeTecnicos = query.getSingleResult().intValue();
			System.out.println(" Impressão de Quantidade de Técnicos : ");
			System.out.println(" Tem  : " + quantidadeDeTecnicos);

			// Estas excessões abaixo podem ocorrer quando usado
			// query.GetSingleResult;
			
			// Tecnicoa para Associar
			String jpql9 = "select u from Usuario u join fetch u.empresa join fetch u.chefe c join fetch c.chefe";
			TypedQuery<Usuario> query9 = manager.createQuery(jpql9,
					Usuario.class);
			List<Usuario> usuarios9 = query9.getResultList();
			for (Usuario usuTecnico : usuarios9) {
				System.out.println(" Impressão de Técnicos da Empresa: ");
				System.out.println(" Nome : " + usuTecnico.getCodigo());
				System.out.println(" Nome : " + usuTecnico.getNome());

			}
						
			
		} catch (NoResultException e) {

			System.out.println(" Objeto não Encontrado!!! : ");

		} catch (NonUniqueResultException e) {

			System.out.println(" Mais de um Objeto Encontrado!!! : ");

		}
	}
}
