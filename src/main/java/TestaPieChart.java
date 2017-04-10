import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Usuario;

public class TestaPieChart {
	public static void main(String[] args) {

		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager em = factory.createEntityManager();
		Session session = em.unwrap(Session.class);
		// SessionFactory hbmSession = session.getSessionFactory();
		//
		// Statistics statistics = hbmSession.getStatistics();
		Usuario tecnicoSelecionado = new Usuario();
		tecnicoSelecionado= em.find(Usuario.class, 9L);
		System.out.println("Encarregado :  " + tecnicoSelecionado.getNome());

		Empresa empresa = new Empresa();
		empresa = em.find(Empresa.class, 1L);
	}

	public static void createPieModel(BigDecimal criticado,
			BigDecimal justificado, BigDecimal pendente, Usuario tecnicoSelecionado) {

		// pieModel.clear();
		// prazoMaior = false;
		//
//		 Map<String, Long> quantidadeCriticadaPorTecnico = this.items
//		 .quantidadeCriticadoFeitoParaEncarregado(tecnicoSelecionado);
//		
		// for (String tecnicos : quantidadeCriticadaPorTecnico.keySet()) {
		//
		// criticado = quantidadeCriticadaPorTecnico.get(tecnicos);
		// pieModel.set(tecnicos, quantidadeCriticadaPorTecnico.get(tecnicos));
		//
		// }
		//
		// Map<String, Long> quantidadeJustificadoPorTecnico = this.items
		// .quantidadeJustificadoFeitoParaEncarregado(tecnicoSelecionado);
		//
		// for (String justificativas :
		// quantidadeJustificadoPorTecnico.keySet()) {
		//
		// justificado = quantidadeJustificadoPorTecnico.get(justificativas);
		// pieModel.set(justificativas,
		// quantidadeJustificadoPorTecnico.get(justificativas));
		//
		// }
		//
		// Map<String, Long> quantidadeTotal = this.items
		// .quantidadePendenteParaEncarregado(tecnicoSelecionado);
		// for (String total : quantidadeTotal.keySet()) {
		//
		//
		// totalGrafico = quantidadeTotal.get(total);
		// pieModel.set(total, quantidadeTotal.get(total));
		//
		//
		// if (getCalculaPercentualdoGrafico().intValue() <=
		// getPercentualProjeto()
		// .intValue() && getPercentualProjeto().intValue() != 0) {
		//
		// prazoMaior = true;
		//
		// FacesUtil
		// .addErrorMessage("Prazo do Projeto Crítico Para o Técnico!!!");
		// }
		//
		// }

	}
}
