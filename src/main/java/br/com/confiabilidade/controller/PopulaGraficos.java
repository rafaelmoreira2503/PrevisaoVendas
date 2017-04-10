package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.model.vo.DataQuantidade;
import br.com.confiabilidade.security.UsuarioLogado;

public class PopulaGraficos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	private Usuario usuario;
	
	
//	Para agrupar as vendas por mês, você precisará alterar a projeção no criteria. Algo como:
//		criteria.setProjection(Projections.projectionList()
//		   .add(Projections.sqlGroupProjection("month(data_criacao) as data", 
//			"month(data_criacao)", new String[] { "data" }, 
//			new Type[] { StandardBasicTypes.INTEGER } ))



	@SuppressWarnings({ "unchecked" })
	public Map<Date, Long> quantidadePorData(Usuario encarregado,
			Empresa empresa) {

		Session session = manager.unwrap(Session.class);

		Calendar dataInicial = Calendar.getInstance();

		System.out.println("-----dataIniEmpresa ----- :  " + empresa.getDataIni());
		dataInicial.setTime(empresa.getDataIni());

		System.out.println("-----Data Ini---- :  " + dataInicial);
		Calendar dataFinal = Calendar.getInstance();
		
		System.out.println("-----data Fim Empresa ----- :  " + empresa.getDataFim());
		dataFinal.setTime(empresa.getDataFim());
		System.out.println("----Data Final Calendar ---- :  " + dataFinal);


		int intervalo = 0;

		intervalo = dataFinal.get(Calendar.DAY_OF_YEAR)
				- dataInicial.get(Calendar.DAY_OF_YEAR);

		System.out.println("Intervalo  :  " + intervalo);

		Map<Date, Long> resultado = criarMapaVazio(intervalo, dataInicial);

		/* Grafico do Encarregado */
		// select DATE(data_item) DATA ,count(i.codigo)
		// from item i
		// join tecnico_item ti on (ti.codigo_item = i.codigo)
		// join usuario tecnico on (tecnico.codigo = ti.codigo_tecnico)
		// join usuario chefe on (chefe.codigo = tecnico.codigo_chefe)
		// where chefe.codigo = 2
		// group by
		// data_item;

		Criteria criteria = session.createCriteria(Item.class);

		criteria.setProjection(Projections
				.projectionList()
				.add(Projections.sqlGroupProjection("date(data_item) as data",
						"date(data_item)", new String[] { "data" },
						new Type[] { StandardBasicTypes.DATE }))

				.add(Projections.count("codigo").as("quantidade")));

		criteria.createAlias("tecnicos", "t");
		criteria.createAlias("t.empresa", "e");
		criteria.createAlias("t.chefe", "c");
		if (encarregado != null) {
			criteria.add(Restrictions.eq("c.codigo", encarregado.getCodigo()));
			System.out.println("Encarregado :  " + encarregado.getNome());
		}

		List<DataQuantidade> quantidadesPorData = criteria
				.setResultTransformer(
						Transformers.aliasToBean(DataQuantidade.class)).list();
		for (DataQuantidade dataQuantidade : quantidadesPorData) {
			
			resultado.put(dataQuantidade.getData(),
					dataQuantidade.getQuantidade());

			System.out.println("Data   -->" + dataQuantidade.getData()
					+ "   Quantidade -->" + dataQuantidade.getQuantidade());

		}

		return resultado;

	}

	@SuppressWarnings({ "unchecked" })
	public Map<Date, Long> todosItems(Empresa empresa) {

		Session session = manager.unwrap(Session.class);

		Calendar dataInicial = Calendar.getInstance();

		System.out.println("======dataIniEmpresa ====== :  " + empresa.getDataIni());
		dataInicial.setTime(empresa.getDataIni());

		System.out.println("====== DataIniCalendar ===== :  " + dataInicial);
		Calendar dataFinal = Calendar.getInstance();
		
		System.out.println("=======data Fim Empresa =======:  " + empresa.getDataFim());
		dataFinal.setTime(empresa.getDataFim());
		System.out.println("======= DataFinal calendar =======  :  " + dataFinal);

		int intervalo = 0;


		intervalo = dataFinal.get(Calendar.DAY_OF_YEAR)
				- dataInicial.get(Calendar.DAY_OF_YEAR);

		System.out.println("Intervalo  :  " + intervalo);

		Map<Date, Long> resultado = criarMapaVazio(intervalo, dataInicial);

		/* Todos Items da Empresa */
		// select DATE(data_item) DATA ,count(i.codigo)
		// from item i
		// join empresa e on i.codigo_empresa = e.codigo
		// where codigo_empresa=1

		Criteria criteria = session.createCriteria(Item.class);


		criteria.setProjection(Projections
				.projectionList()
				.add(Projections.sqlGroupProjection("date(data_item) as data",
						"date(data_item)", new String[] { "data" },
						new Type[] { StandardBasicTypes.DATE }))

				.add(Projections.count("codigo").as("quantidade")));

		criteria.createAlias("empresa", "e");

		criteria.add(Restrictions.eq("e.codigo", empresa.getCodigo()));

		List<DataQuantidade> quantidadesPorData = criteria
				.setResultTransformer(
						Transformers.aliasToBean(DataQuantidade.class)).list();
		for (DataQuantidade dataQuantidade : quantidadesPorData) {
			
			for (int i = 0; i <= 10; i++) {
				resultado.put(dataInicial.getTime(),
						dataQuantidade.getQuantidade());
				dataInicial.add(Calendar.DATE, 1);
			}
			

			System.out.println("Data   -->" + dataQuantidade.getData()
					+ "   Quantidade -->" + dataQuantidade.getQuantidade());
		}

		return resultado;

	}

	private Map<Date, Long> criarMapaVazio1(Integer numeroDeDias,
			Calendar dataInicial) { // Cria um mapa vazio para e não existir
									// informação o grafico de linhas, a linha
									// ir no zero.

		dataInicial = (Calendar) dataInicial.clone();

		Map<Date, Long> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= 10; i++) {
			mapaInicial.put(dataInicial.getTime(), 0L);
			dataInicial.add(Calendar.DATE, 1);
		}

		return mapaInicial;
	}
	
	private Map<Date, Long> criarMapaVazio(Integer numeroDeDias,
			Calendar dataInicial) { // Cria um mapa vazio para e não existir
									// informação o grafico de linhas, a linha
									// ir no zero.

		dataInicial = (Calendar) dataInicial.clone();

		Map<Date, Long> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= 10; i++) {
			mapaInicial.put(dataInicial.getTime(), 0L);
			dataInicial.add(Calendar.DATE, 1);
		}

		return mapaInicial;
	}

}
