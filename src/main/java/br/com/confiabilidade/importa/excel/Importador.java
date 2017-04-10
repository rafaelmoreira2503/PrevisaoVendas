package br.com.confiabilidade.importa.excel;

import java.io.InputStream;


public abstract class Importador {
	
	protected abstract Importar criaImportacao();
	

		public void importar(InputStream input){
			
		}

}
