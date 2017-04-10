package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.primefaces.model.chart.PieChartModel;

import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class EstatisticaParaGerenteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	@Inject
	private Items items;

	private boolean prazoDoEncarrgadoMaior;
	private boolean PrazoDoTecnicoMaior; // Diz se o percentual dos items feitos
											// é menor

	@Inject
	private Usuarios encarregados;

	@NotNull
	private Usuario encarregadoSelecionado;

	private List<Usuario> encarregadosDaEmpresa;

	private List<Usuario> tecnicos;

	private Usuario tecnicoSelecionado;

	private PieChartModel graficoDoTecnico;

	private BigDecimal duracaoProjetoParaTecnico;
	private BigDecimal decorridoParaTecnico;
	private BigDecimal duracaoProjetoEncarregado;
	private BigDecimal decorridoParaEncarregado;
	private BigDecimal resultado;

	private BigDecimal criticado;
	private BigDecimal justificado;
	private BigDecimal pendenteParaTecnico;

	private BigDecimal naoSeAplica;
	private BigDecimal quantidadeTecnicos;
	private BigDecimal pendenteParaEncarregado;

	private PieChartModel graficoDoEncarregado;

	public EstatisticaParaGerenteBean() {

		this.graficoDoTecnico = new PieChartModel();
		this.graficoDoEncarregado = new PieChartModel();

	}

	public void carregarTecnicos() {

		tecnicos = encarregados.tecnicosdoEncarregado(encarregadoSelecionado
				.getCodigo());

	}

	public PieChartModel getgraficoDoEncarregado() {
		return graficoDoEncarregado;
	}

	public void preRender() {
		if (FacesUtil.isNotPostback()) {
			encarregadosDaEmpresa = encarregados.carregarEncarregado(usuario
					.getEmpresa().getCodigo());
			duracaoProjetoParaTecnico = BigDecimal.valueOf(items
					.duracaoDoProjeto(usuario));

			decorridoParaTecnico = BigDecimal.valueOf(items
					.decorridoDoProjeto(usuario));

			duracaoProjetoEncarregado = BigDecimal.valueOf(
					items.duracaoDoProjeto(usuario)).subtract(
					BigDecimal.valueOf(2));

			decorridoParaEncarregado = BigDecimal.valueOf(items
					.decorridoDoProjeto(usuario));

		}

	}

	public void criaGraficos() {

		createGraficoDoTecnico();
		
		createGraficoDoEncarregado();
	}

	public PieChartModel getGraficoDoTecnico() {
		return graficoDoTecnico;
	}

	public void createGraficoDoTecnico() {

		graficoDoTecnico.clear();
		PrazoDoTecnicoMaior = true;

		Map<String, Long> quantidadeCriticadaPorTecnico = this.items
				.quantidadeCriticadoFeitoParaEncarregado(tecnicoSelecionado);

		for (String tecnicos : quantidadeCriticadaPorTecnico.keySet()) {

			criticado = BigDecimal.valueOf(quantidadeCriticadaPorTecnico
					.get(tecnicos));

			graficoDoTecnico.set(tecnicos,
					quantidadeCriticadaPorTecnico.get(tecnicos));

		}

		Map<String, Long> quantidadeJustificadoPorTecnico = this.items
				.quantidadeJustificadoFeitoParaEncarregado(tecnicoSelecionado);

		for (String justificativas : quantidadeJustificadoPorTecnico.keySet()) {

			justificado = BigDecimal.valueOf(quantidadeJustificadoPorTecnico
					.get(justificativas));

			graficoDoTecnico.set(justificativas,
					quantidadeJustificadoPorTecnico.get(justificativas));

		}

		Map<String, Long> quantidadeTotal = this.items
				.quantidadePendenteParaEncarregado(tecnicoSelecionado);
		for (String total : quantidadeTotal.keySet()) {

			pendenteParaTecnico = BigDecimal
					.valueOf(quantidadeTotal.get(total));

			graficoDoTecnico.set(total, quantidadeTotal.get(total));

		}
		if (getCalculaPercentualDoTecnico().intValue() <= getPercentualDoProjetoParaTecnico()
				.intValue() && getCalculaPercentualDoTecnico().intValue() != 0) {

			PrazoDoTecnicoMaior = true;

			FacesUtil
					.addErrorMessage("Prazo do Projeto Crítico Para o Técnico!!!");
		}

	}

	// // Calcula o percentual do Projeto baseado na data ini e fim da empresa
	public BigDecimal getPercentualDoProjetoParaTecnico() {

		resultado = BigDecimal.valueOf((decorridoParaTecnico.doubleValue()
				/ duracaoProjetoParaTecnico.doubleValue() * 100));
		resultado = resultado.setScale(0, BigDecimal.ROUND_HALF_UP);

		return resultado;

	}

	public BigDecimal getPercentualDoProjetoParaEncarregado() {

		resultado = BigDecimal.valueOf((decorridoParaEncarregado.doubleValue()
				/ duracaoProjetoEncarregado.doubleValue() * 100));
		resultado = resultado.setScale(0, BigDecimal.ROUND_HALF_UP);

		return resultado;

	}

	// Calcula o percentual de andamento dos itemsfeitos mostrado no grafico
	// Para o Técnico
	private BigDecimal getCalculaPercentualDoTecnico() {

		BigDecimal primeiro = new BigDecimal(criticado.intValue());
		BigDecimal segundo = new BigDecimal(justificado.intValue());
		BigDecimal terceiro = new BigDecimal(pendenteParaTecnico.intValue());
		BigDecimal soma = primeiro.add(segundo).add(terceiro);
		BigDecimal percent = new BigDecimal("100");
		// BigDecimal resultado = BigDecimal.ZERO;
		if (BigDecimal.ZERO.equals(soma)) {
			throw new NegocioException(
					"Calculo não pode ser feito deve existir pelo menos um Item Associado ou criticado.");
		}
		BigDecimal percentualGrafico = primeiro.add(segundo)
				.divide(soma, 2, BigDecimal.ROUND_HALF_UP).multiply(percent);// Coloca
																				// duas
																				// casas
																				// e
																				// retira
																				// se
																				// for
																				// dizima
																				// caso
																				// Contrario
																				// recebo
																				// Non-terminating
																				// decimal
																				// expansion;
		//System.out.println("Percentual do Grafico" + percentualGrafico); // no
																			// exact
																			// representable
																			// decimal
																			// result
		percentualGrafico = percentualGrafico.setScale(0,
				BigDecimal.ROUND_HALF_UP);// arredonda para cima e tira o ponto
											// decimal resultando em um inteiro

		return percentualGrafico; // expansion;

	}

	// Calcula o percentual de andamento dos itemsfeitos mostrado no grafico
	// Para o Encarregado
	private BigDecimal getCalculaPercentualDoEncarregado() {

		BigDecimal primeiro = new BigDecimal(naoSeAplica.intValue());
		BigDecimal segundo = new BigDecimal(quantidadeTecnicos.intValue());
		BigDecimal terceiro = new BigDecimal(pendenteParaEncarregado.intValue());
		BigDecimal soma = primeiro.add(segundo).add(terceiro);
		BigDecimal percent = new BigDecimal("100");
		BigDecimal percentualGraficoEncarregado = primeiro.add(segundo)
				.divide(soma, 2, BigDecimal.ROUND_HALF_UP).multiply(percent);// Coloca
																				// duas
																				// casas
																				// e
																				// retira
																				// se
																				// for
																				// dizima
																				// caso
																				// Contrario
																				// recebo
																				// Non-terminating
																				// decimal
																				// expansion;
//		System.out.println("Percentual do Grafico"
//				+ percentualGraficoEncarregado); // no
		// exact
		// representable
		// decimal
		// result
		percentualGraficoEncarregado = percentualGraficoEncarregado.setScale(0,
				BigDecimal.ROUND_HALF_UP);// arredonda para cima e tira o ponto

		return percentualGraficoEncarregado;

	}

	public void createGraficoDoEncarregado() {

		graficoDoEncarregado.clear();
		prazoDoEncarrgadoMaior = false;

		Map<String, Long> quantidadeNaoAplicavel = this.items
				.quantidadeNaoAplicavelParaEncarregado(encarregadoSelecionado);

		for (String naoSeAplica : quantidadeNaoAplicavel.keySet()) {

			this.naoSeAplica = BigDecimal.valueOf(quantidadeNaoAplicavel
					.get(naoSeAplica));
			graficoDoEncarregado.set(naoSeAplica,
					quantidadeNaoAplicavel.get(naoSeAplica));

		}

		Map<String, Long> quantidadePorTecnico = this.items
				.quantidadeTecnicos(encarregadoSelecionado);

		for (String tecnicos : quantidadePorTecnico.keySet()) {

			this.quantidadeTecnicos = BigDecimal.valueOf((quantidadePorTecnico
					.get(tecnicos)));

			graficoDoEncarregado.set(tecnicos,
					quantidadePorTecnico.get(tecnicos));

		}

		Map<String, Long> quantidadeTotal = this.items
				.quantidadeTotalParaEncarregdo(encarregadoSelecionado);
		for (String total : quantidadeTotal.keySet()) {

			this.pendenteParaEncarregado = BigDecimal.valueOf(quantidadeTotal
					.get(total));
			graficoDoEncarregado.set(total, quantidadeTotal.get(total));

		}

		if (getCalculaPercentualDoEncarregado().intValue() <= getPercentualDoProjetoParaEncarregado()
				.intValue()
				&& getCalculaPercentualDoEncarregado().intValue() != 0) {

			prazoDoEncarrgadoMaior = true;

			FacesUtil
					.addErrorMessage("Prazo do Projeto Crítico Para o Encarregado!!!");
		}

	}

	@NotNull
	public Usuario getTecnicoSelecionado() {
		return tecnicoSelecionado;
	}

	public List<Usuario> getTecnicos() {
		return tecnicos;
	}

	public void setTecnicoSelecionado(Usuario tecnicoSelecionado) {
		this.tecnicoSelecionado = tecnicoSelecionado;
	}

	public Number getDuracaoProjetoParaTecnico() {
		return duracaoProjetoParaTecnico;
	}

	public Number getDecorridoParaTecnico() {
		return decorridoParaTecnico;
	}

	public boolean isPrazoDoTecnicoMaior() {
		return PrazoDoTecnicoMaior;
	}

	public Number getDuracaoProjetoEncarregado() {
		return duracaoProjetoEncarregado;
	}

	public Number getDecorridoParaEncarregado() {
		return decorridoParaEncarregado;
	}

	public List<Usuario> getEncarregadosDaEmpresa() {
		return encarregadosDaEmpresa;
	}

	public Usuario getEncarregadoSelecionado() {
		return encarregadoSelecionado;
	}

	public void setEncarregadoSelecionado(Usuario encarregadoSelecionado) {
		this.encarregadoSelecionado = encarregadoSelecionado;

	}

	public boolean isPrazoDoEncarrgadoMaior() {
		return prazoDoEncarrgadoMaior;
	}
}