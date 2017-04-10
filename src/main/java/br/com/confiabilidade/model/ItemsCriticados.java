//package br.com.confiabilidade.model;
//
//import java.io.Serializable;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "CriticidadeAbcde")
//public class ItemsCriticados implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	private Long codigo;
//	private Item codigo_item;
//	private ItemAbc codigo_itemAbc;
//	private Empresa codigo_empresa;
//
//	
//
//	@Id
//	@GeneratedValue
//	public Long getCodigo() {
//		return codigo;
//	}
//
//	public void setCodigo(Long codigo) {
//		this.codigo = codigo;
//	}
//	@ManyToOne
//	@JoinColumn(name = "codigo_item")
//	public Item getCodigo_item() {
//		return codigo_item;
//	}
//
//	public void setCodigo_item(Item codigo_item) {
//		this.codigo_item = codigo_item;
//	}
//	@ManyToOne
//	@JoinColumn(name = "codigo_itemAbc")
//	public ItemAbc getCodigo_itemAbc() {
//		return codigo_itemAbc;
//	}
//
//	public void setCodigo_itemAbc(ItemAbc codigo_itemAbc) {
//		this.codigo_itemAbc = codigo_itemAbc;
//	}
//	@ManyToOne
//	@JoinColumn(name = "codigo_empresa")
//	public Empresa getCodigo_empresa() {
//		return codigo_empresa;
//	}
//
//	public void setCodigo_empresa(Empresa codigo_empresa) {
//		this.codigo_empresa = codigo_empresa;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (obj == null) {
//			return false;
//		}
//		if (getClass() != obj.getClass()) {
//			return false;
//		}
//		ItemsCriticados other = (ItemsCriticados) obj;
//		if (codigo == null) {
//			if (other.codigo != null) {
//				return false;
//			}
//		} else if (!codigo.equals(other.codigo)) {
//			return false;
//		}
//		return true;
//	}
//
//	
//
//	
//}
