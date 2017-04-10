package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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
public class EstatisticaParaEncarregadoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	private Usuario encarregado;

	@Inject
	private Usuarios tecnicos;

	private Usuario tecnicoSelecionado;

	private BigDecimal duracaoProjetoParaTecnico;
	private BigDecimal decorridoParaTecnico;
	private BigDecimal duracaoProjetoEncarregado;
	private BigDecimal decorridoParaEncarregado;
	private BigDecimal resultado;

	private boolean prazoDoEncarregadoMaior;
	private boolean PrazoDoTecnicoMaior;

	public boolean isPrazoDoEncarrgadoMaior() {
		return prazoDoEncarregadoMaior;
	}

	public boolean isPrazoDoTecnicoMaior() {
		return PrazoDoTecnicoMaior;
	}

	private BigDecimal criticado;
	private BigDecimal justificado;
	private BigDecimal pendenteParaTecnico;

	private BigDecimal naoSeAplica;
	private BigDecimal quantidadeTecnicos;
	private BigDecimal pendenteParaEncarregado;

	private List<Usuario> tecnicosDaEmpresa;

	@Inject
	private Items items;

	private PieChartModel graficoDoTecnico;

	private PieChartModel graficoDoEncarregado;

	public EstatisticaParaEncarregadoBean() {

		this.graficoDoEncarregado = new PieChartModel();
		this.graficoDoTecnico = new PieChartModel();

	}

	public PieChartModel getGraficoDoEncaregado() {
		return graficoDoEncarregado;
	}

	public PieChartModel getGraficoDoTecnico() {
		return graficoDoTecnico;
	}

	

	@PostConstruct
	public void preRender() {
		if (FacesUtil.isNotPostback()) {
			tecnicosDaEmpresa = tecnicos.tecnicosdoEncarregado(encarregado
					.getCodigo());
			duracaoProjetoParaTecnico = BigDecimal.valueOf(items
					.duracaoDoProjeto(encarregado));

			decorridoParaTecnico = BigDecimal.valueOf(items
					.decorridoDoProjeto(encarregado));

			duracaoProjetoEncarregado = BigDecimal.valueOf(
					items.duracaoDoProjeto(encarregado)).subtract(
					BigDecimal.valueOf(2));

			decorridoParaEncarregado = BigDecimal.valueOf(items
					.decorridoDoProjeto(encarregado));

		}

	}

	public void criaGraficos() {

		createGraficoDoTecnico();
		createGraficoDoEncarregado();

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

			pendenteParaTecnico = BigDecimal.valueOf(quantidadeTotal.get(total));

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

		percentualGrafico = percentualGrafico.setScale(0,
				BigDecimal.ROUND_HALF_UP);// arredonda para cima e tira o
											// ponto
											// decimal resultando em um
											// inteiro

		return percentualGrafico;

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
				.divide(soma, 2, BigDecimal.ROUND_HALF_UP).multiply(percent);

		percentualGraficoEncarregado = percentualGraficoEncarregado.setScale(0,
				BigDecimal.ROUND_HALF_UP);// arredonda para cima e tira o ponto
											// decimal resultando em um inteiro

		return percentualGraficoEncarregado;

	}

	public void createGraficoDoEncarregado() {

		graficoDoEncarregado.clear();
		prazoDoEncarregadoMaior = false;

		Map<String, Long> quantidadeNaoAplicavel = this.items
				.quantidadeNaoAplicavelParaEncarregado(encarregado);

		for (String naoSeAplica : quantidadeNaoAplicavel.keySet()) {

			this.naoSeAplica = BigDecimal.valueOf(quantidadeNaoAplicavel
					.get(naoSeAplica));
			graficoDoEncarregado.set(naoSeAplica,
					quantidadeNaoAplicavel.get(naoSeAplica));

		}

		Map<String, Long> quantidadePorTecnico = this.items
				.quantidadeTecnicos(encarregado);

		for (String tecnicos : quantidadePorTecnico.keySet()) {

			this.quantidadeTecnicos = BigDecimal.valueOf((quantidadePorTecnico
					.get(tecnicos)));

			graficoDoEncarregado.set(tecnicos,
					quantidadePorTecnico.get(tecnicos));

		}

		Map<String, Long> quantidadeTotal = this.items
				.quantidadeTotalParaEncarregdo(encarregado);
		for (String total : quantidadeTotal.keySet()) {

			this.pendenteParaEncarregado = BigDecimal.valueOf(quantidadeTotal
					.get(total));
			graficoDoEncarregado.set(total, quantidadeTotal.get(total));

		}

		if (getCalculaPercentualDoEncarregado().intValue() <= getPercentualDoProjetoParaEncarregado()
				.intValue()
				&& getCalculaPercentualDoEncarregado().intValue() != 0) {

			prazoDoEncarregadoMaior = true;

			FacesUtil
					.addErrorMessage("Prazo do Projeto Crítico Para o Encarregado!!!");
		}

	}

	public List<Usuario> getTecnicosDaEmpresa() {
		return tecnicosDaEmpresa;
	}

	@NotNull
	public Usuario getTecnicoSelecionado() {
		return tecnicoSelecionado;
	}

	public void setTecnicoSelecionado(Usuario tecnicoSelecionado) {
		this.tecnicoSelecionado = tecnicoSelecionado;
	}

	public BigDecimal getDuracaoProjetoParaTecnico() {
		return duracaoProjetoParaTecnico;
	}

	public BigDecimal getDecorridoParaTecnico() {
		return decorridoParaTecnico;
	}

	public BigDecimal getDuracaoProjetoEncarregado() {
		return duracaoProjetoEncarregado;
	}

	public BigDecimal getDecorridoParaEncarregado() {
		return decorridoParaEncarregado;
	}

}