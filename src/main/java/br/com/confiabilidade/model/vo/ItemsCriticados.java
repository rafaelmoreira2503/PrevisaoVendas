package br.com.confiabilidade.model.vo;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.ItemAbc;

public class ItemsCriticados {
	
	private Item item;
	
	public ItemsCriticados(Item item, ItemAbc itemAbc) {
		super();
		this.item = item;
		this.itemAbc = itemAbc;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ItemAbc getItemAbc() {
		return itemAbc;
	}

	public void setItemAbc(ItemAbc itemAbc) {
		this.itemAbc = itemAbc;
	}

	private ItemAbc itemAbc;

}
