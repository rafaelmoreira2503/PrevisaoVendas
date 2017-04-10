package br.com.confiabilidade.importar;

import java.io.InputStream;

import br.com.confiabilidade.model.Item;

public interface Importavel {
	public void Upload(InputStream in, Item item);

}
