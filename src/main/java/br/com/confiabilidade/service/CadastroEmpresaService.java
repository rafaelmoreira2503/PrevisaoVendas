package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.caelum.stella.bean.validation.IE;
import br.com.caelum.stella.validation.ie.IEAcreValidator;
import br.com.caelum.stella.validation.ie.IEAlagoasValidator;
import br.com.caelum.stella.validation.ie.IEAmapaValidator;
import br.com.caelum.stella.validation.ie.IEAmazonasValidator;
import br.com.caelum.stella.validation.ie.IEBahiaValidator;
import br.com.caelum.stella.validation.ie.IECearaValidator;
import br.com.caelum.stella.validation.ie.IEDistritoFederalValidator;
import br.com.caelum.stella.validation.ie.IEEspiritoSantoValidator;
import br.com.caelum.stella.validation.ie.IEGoiasValidator;
import br.com.caelum.stella.validation.ie.IEMaranhaoValidator;
import br.com.caelum.stella.validation.ie.IEMatoGrossoDoSulValidator;
import br.com.caelum.stella.validation.ie.IEMatoGrossoValidator;
import br.com.caelum.stella.validation.ie.IEMinasGeraisValidator;
import br.com.caelum.stella.validation.ie.IEParaValidator;
import br.com.caelum.stella.validation.ie.IEParaibaValidator;
import br.com.caelum.stella.validation.ie.IEParanaValidator;
import br.com.caelum.stella.validation.ie.IEPernambucoValidator;
import br.com.caelum.stella.validation.ie.IEPiauiValidator;
import br.com.caelum.stella.validation.ie.IERioDeJaneiroValidator;
import br.com.caelum.stella.validation.ie.IERioGrandeDoNorteValidator;
import br.com.caelum.stella.validation.ie.IERioGrandeDoSulValidator;
import br.com.caelum.stella.validation.ie.IERondoniaValidator;
import br.com.caelum.stella.validation.ie.IERoraimaValidator;
import br.com.caelum.stella.validation.ie.IESantaCatarinaValidator;
import br.com.caelum.stella.validation.ie.IESaoPauloValidator;
import br.com.caelum.stella.validation.ie.IESergipeValidator;
import br.com.caelum.stella.validation.ie.IETocantinsValidator;
import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

@IE(ieField = "inscricaoEstadual", estadoField = "uf")
public class CadastroEmpresaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;
	
	@Transactional
	public void associarPergunta(Empresa empresa) { 

		empresas.guardar(empresa);


	}

	@Transactional
	public Empresa salvar(Empresa empresa) {

		if(empresa.getDataIni().after(empresa.getDataFim())){
			
			throw new NegocioException("Data de Inicio não pode ser superior a Data Fim");
		}
		// Onde \\D vai retirar todo e qualquer caracter que não seja número  
		String ie = empresa.getIscricaoEstadual().replaceAll("\\D", "");    
		String uf = empresa.getEndereco().getUf();
		
	
		

		if (!validaIE(ie, uf)) {

			throw new NegocioException(FacesUtil.getMensagemI18n("ie_invalido"));
		}

		Empresa empresaExistente = empresas.porCnpj(empresa.getCnpj());

		if (empresaExistente != null && !empresaExistente.equals(empresa)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("cnpj_existente"));
		}

		return this.empresas.guardar(empresa);
	}

	public static boolean validaIE(String ie, String uf) {

		try {

			if (uf.toUpperCase().equals("AC")) {
				new IEAcreValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("AL")) {
				new IEAlagoasValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("AP")) {
				new IEAmapaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("AM")) {
				new IEAmazonasValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("BA")) {
				new IEBahiaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("CE")) {
				new IECearaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("DF")) {
				new IEDistritoFederalValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("ES")) {
				new IEEspiritoSantoValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("GO")) {
				new IEGoiasValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("MA")) {
				new IEMaranhaoValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("MS")) {
				new IEMatoGrossoDoSulValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("MT")) {
				new IEMatoGrossoValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("MG")) {
				new IEMinasGeraisValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("PA")) {
				new IEParaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("PB")) {
				new IEParaibaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("PR")) {
				new IEParanaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("PE")) {
				new IEPernambucoValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("PI")) {
				new IEPiauiValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("RJ")) {
				new IERioDeJaneiroValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("RN")) {
				new IERioGrandeDoNorteValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("RS")) {
				new IERioGrandeDoSulValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("RO")) {
				new IERondoniaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("RR")) {
				new IERoraimaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("SC")) {
				new IESantaCatarinaValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("SP")) {
				new IESaoPauloValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("SE")) {
				new IESergipeValidator(false).assertValid(ie);
			}
			if (uf.toUpperCase().equals("TO")) {
				new IETocantinsValidator(false).assertValid(ie);
			}

			return true;

		} catch (Exception e) {
			return false;
		}
	}
}
