package br.com.confiabilidade.importa.excel;

public class ImportadorXls extends Importador{

	@Override
	protected Importar criaImportacao() {
		
		return new Importa2007();
	}

}
