import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Endereco;
import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.model.PerguntaEmpresa;
import br.com.confiabilidade.model.Permissao;
import br.com.confiabilidade.model.Telefone;
import br.com.confiabilidade.model.Usuario;

public class CargadeDados {
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("ConfiabilidadePU");
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Calendar dataInicial = Calendar.getInstance();

		Calendar dataFinal = Calendar.getInstance();
		dataFinal.add(Calendar.DAY_OF_MONTH, 50);

		
		Endereco endereco = new Endereco();
		endereco.setCep("2223-0061");
		endereco.setComplemento("apartamento 209");
		endereco.setBairro("Rio deJaneiro");
		endereco.setNumero("4000");
		endereco.setUf("RJ");
		endereco.setLogradouro("Rua da Carioca");

		Empresa empresa = new Empresa();
		empresa.setCnpj("92.714.916/0001-21");
		empresa.setIscricaoEstadual("81797013");
		empresa.setNome("GROFFE Metais LTDA");
		empresa.setNomeFantasia("GROFFE");
		empresa.setSegmento("Metalurgia");
		empresa.setDataIni(dataInicial.getTime());
		empresa.setDataFim(dataFinal.getTime());
		empresa.setSponsor("Sponsor Francisco");
		empresa.setEndereco(endereco);
		empresa.setTotalItems(1500);
		em.persist(empresa);

		Empresa empresa2 = new Empresa();
		empresa2.setCnpj("61.190.096/0001-92");
		empresa2.setIscricaoEstadual("104636300110");
		empresa2.setNome("EUROFARMA LABORATORIOS S.A.");
		empresa2.setNomeFantasia("EUROFARMA LABORATORIOS S.A.");
		empresa2.setSegmento("Farmaco");
		empresa2.setDataIni(dataInicial.getTime());
		empresa2.setDataFim(dataFinal.getTime());
		empresa2.setSponsor("Pitangui");
		empresa2.setEndereco(endereco);
		empresa2.setTotalItems(1500);

		em.persist(empresa2);

		// empresa.getPerguntaEmpresas().add(perguntaEmpresa);

		Pergunta pergunta = new Pergunta();
		pergunta.setDescricao("A FALTA DO ITEM GERA PERDA DO MATERIAL ");

		em.merge(pergunta);
		PerguntaEmpresa perguntaEmpresa = new PerguntaEmpresa();
		Empresa empresa1 = em.find(Empresa.class,1L);
		Pergunta pergunta1 = em.find(Pergunta.class, 1L);
		
		perguntaEmpresa.setCodigo_empresa(empresa1);
		perguntaEmpresa.setCodigo_pergunta(pergunta1);
		em.persist(perguntaEmpresa);

		Pergunta pergunta2 = new Pergunta();
		pergunta2.setDescricao("A FALTA DO ITEM IMPACTA NA PRODUÇÃO ");

		em.persist(pergunta2);

		Pergunta pergunta3 = new Pergunta();
		pergunta3.setDescricao("A FALTA DO ITEM IMPACTA QUALIDADE ");

		em.persist(pergunta3);

		Pergunta pergunta4 = new Pergunta();
		pergunta4.setDescricao("A FALTA DO ITEM GERA PERDA DE INSUMOS ");

		em.persist(pergunta4);

		Pergunta pergunta5 = new Pergunta();
		pergunta5.setDescricao("A FALTA DO ITEM GERA PERDA DE MERCADO ");

		em.persist(pergunta5);
		Especialidade especialidade = new Especialidade();
		especialidade.setDescricao("INSTRUMENTAÇÃO");
		em.persist(especialidade);

		Especialidade especialidade2 = new Especialidade();
		especialidade2.setDescricao("MECÂNICA");
		em.persist(especialidade2);

		Area area = new Area();
		area.setDescricao("ACIARIA");
		em.persist(area);

		Area area2 = new Area();
		area2.setDescricao("AUTO FORNO");
		em.persist(area2);

		Telefone telefone = new Telefone();
		telefone.setCelular("21-99806-4688");
		telefone.setFixo("21-3244-2244");
		telefone.setRamal("7777");

		// Pai
		// O PERSIST/MERGE É DO PAI PARA O FILHO , OU SEJA PERSIST(PAI) E SE O
		// FILHO NÃO ESTIVER
		//SSALVO AO FIZER PAI.SET(FILHO) RECEBEMOS ERRO OBJECT REFERENCES A
		// INSTANCE TRANSIENT UNSAVED 
		Usuario gerente = new Usuario();
		gerente.setNome("PAULO RAMOS");
		gerente.setEmail("paulo@gmail.com");
		gerente.setSenha("123456");
		gerente.setPermissao(Permissao.GERENTE);
		gerente.setEmpresa(empresa);
		gerente.setTelefone(telefone);

		// Filho
		Usuario encarregado = new Usuario();

		encarregado.setNome("FRANCISCO SILVA");
		encarregado.setEmail("teste@gmail.com");
		encarregado.setSenha("123");
		encarregado.setTelefone(telefone);
		encarregado.setAreaEncarregado(area);
		encarregado.setPermissao(Permissao.ENCARREGADO);
		encarregado.setEmpresa(empresa);
		encarregado.setChefe(gerente); 
		
		gerente.getSubordinados().add(encarregado);
		

		Usuario encarregado2 = new Usuario();

		encarregado2.setNome("MARCOS GAMA");
		encarregado2.setEmail("teste2@gmail.com");
		encarregado2.setSenha("123");
		encarregado2.setTelefone(telefone);
		encarregado2.setAreaEncarregado(area2);
		encarregado2.setPermissao(Permissao.ENCARREGADO);
		encarregado2.setEmpresa(empresa);
		encarregado2.setChefe(gerente);
		gerente.getSubordinados().add(encarregado2);
		em.merge(gerente);

		Usuario encarregado4 = em.find(Usuario.class, 2L);
		
		Usuario encarregado5 = em.find(Usuario.class, 3L);
		
		Usuario tecnico = new Usuario();

		tecnico.setNome("VICENTE CARVALHO");
		tecnico.setEmail("tecnico@gmail.com");
		tecnico.setSenha("123");
		tecnico.setEspecialidade(especialidade2);//mecanica
		tecnico.setPermissao(Permissao.TECNICO);
		tecnico.setEmpresa(empresa);
		tecnico.setTelefone(telefone);
		tecnico.setChefe(encarregado4);
		encarregado.getSubordinados().add(tecnico);
		em.merge(tecnico);

		Usuario tecnico2 = new Usuario();

		tecnico2.setNome("FABIO LIMA");
		tecnico2.setEmail("tecnico2@gmail.com");
		tecnico2.setSenha("123");
		tecnico2.setEspecialidade(especialidade);//instrumentação
		tecnico2.setPermissao(Permissao.TECNICO);
		tecnico2.setEmpresa(empresa);
		tecnico2.setTelefone(telefone);
		tecnico2.setChefe(encarregado4);
		
		encarregado.getSubordinados().add(tecnico2);
		em.merge(tecnico2); //Técnico do Frencisco

		Usuario tecnico3 = new Usuario();

		tecnico3.setNome("BATISTA ARAUJO");
		tecnico3.setEmail("batista@gmail.com");
		tecnico3.setSenha("123");
		tecnico3.setEspecialidade(especialidade2);
		tecnico3.setPermissao(Permissao.TECNICO);
		tecnico3.setEmpresa(empresa);
		tecnico3.setTelefone(telefone);
		tecnico3.setChefe(encarregado5);
		
		encarregado2.getSubordinados().add(tecnico3);
		em.merge(tecnico3);

		Usuario omc = new Usuario();

		omc.setNome("RAFAEL MOREIRA");
		omc.setEmail("rafa@gmail.com");
		omc.setSenha("123");
		omc.setPermissao(Permissao.OMC);

		omc.setTelefone(telefone);

		em.persist(omc);

		tx.commit();

	}
}
