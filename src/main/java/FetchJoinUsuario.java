import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Usuario;

public class FetchJoinUsuario {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager manager = factory.createEntityManager();

		EntityTransaction trx = manager.getTransaction();
		trx.begin();

		Usuario encarregado = manager.find(Usuario.class, 3L);

//		Query query1 = manager
//				.createQuery(
//						"select i.tecnico from Item i join fetch i.empresa e left join fetch i.tecnicos u left join u.chefe c where c.codigo=:codigo ",
//						Item.class).setParameter("codigo", 2L);
//
//		List<Item> items = query1.getResultList();
//
//		System.out.println("---Impressão de items do Encarregado----");
//
//		// Todas as Areas do Encarregado
//
//		for (Item item : items) {
//
//			System.out.println("Codigo :" + item.getCodigo() + " CódigoItem : "
//					+ item.getCodigoItem());
//
//			for (Usuario usuario : item.getTecnico()) {
//
//				System.out.println("  CodigoTecnico :" + usuario.getCodigo()
//						+ "   CodigoChefe: " + usuario.getChefe().getCodigo()
//						+ " Nome chefe  : " + usuario.getChefe().getNome());
//			}
//
//		}
		Query query2 = manager

				.createQuery(
						"select distinct(u) from Usuario u join fetch u.items join fetch u.chefe c  ",
						Usuario.class);

		List<Usuario> usuarios = query2.getResultList();

		for (Usuario usuario : usuarios) {
			System.out.println(" codigoUsuario  : " + usuario.getCodigo()
					+ "  Nome : " + usuario.getNome());

		}

	}

}
