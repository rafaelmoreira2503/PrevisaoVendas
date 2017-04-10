import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Usuario;

public class FetchJoinTecnicosItem {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager manager = factory.createEntityManager();

		EntityTransaction trx = manager.getTransaction();
		trx.begin();
		
//		 Query query4 = manager.createQuery("from Item i where not exists (from Usuario u where not exists(from tecnico_item it where it.items.codigo = i.codigo and it.tecnicos.codigo = usuario.codigo))");
//		 List<Item> especialidades2 = query4.getResultList();
//		 System.out.println("---Impressão de Pergunta----");
//				 for (Item especialidade : especialidades2) {
//		 System.out.println(" Codigo : " + especialidade.getCodigo());
//		 System.out.println(" Descrição : " + especialidade.getDescricao());
//		
//		 }

		// Técnicos com seus items para desassociar
		String jpql1 = "select distinct(u) from Usuario u left join u.items left join fetch u.chefe c where c.codigo=:codigo";
		TypedQuery<Usuario> query1 = manager.createQuery(jpql1, Usuario.class);
		query1.setParameter("codigo", 2L);
				
		
		System.out.println("*----- Impressão de tecnicos PARA DESASSOCIAR ----/*");
		List<Usuario> tecnicos = query1.getResultList();
		for (Usuario tecnicosComItems : tecnicos) {
			
			System.out.println(" Nome : " + tecnicosComItems.getChefe().getAreaEncarregado().getDescricao());
			System.out.println(" Nome : " + tecnicosComItems.getNome());
			
		}
		System.out.println("*----- FIM da Impressão de tecnicos PARA DESASSOCIAR ----/*");
		
//		// Técnicos com seus items pra associar
//		String jpql11 = "select u from Usuario u join fetch u.empresa where u.permissao='TECNICO' and u not in (select distinct(u) from Item i join i.tecnicos u where i.codigo=:codigo)";
//		TypedQuery<Usuario> query11 = manager
//				.createQuery(jpql11, Usuario.class).setParameter("codigo", 3L);
//		System.out.println("*----- Impressão de tecnicos PARA ASSOCIAR ----/*");
//		List<Usuario> tecnicos1 = query11.getResultList();
//		for (Usuario tecnicosComItems1 : tecnicos1) {
//			
//			System.out.println(" Nome : " + tecnicosComItems1.getCodigo());
//			System.out.println(" Nome : " + tecnicosComItems1.getNome());
//			
//			
//		}
//		System.out.println("*----- FIM da Impressão de tecnicos PARA ASSOCIAR ----/*");
//		manager.close();
	}

}
