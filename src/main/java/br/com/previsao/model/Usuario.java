package br.com.previsao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "usuario")
@NamedQueries({
		@NamedQuery(name = Usuario.CARREGARDIRETOR, query = "select u from Usuario u where u.chefe is null and u.empresa.codigo=:codigo "),
		@NamedQuery(name = Usuario.CARREGARGERENTEFILIAL, query = "select u from Usuario u where u.permissao=br.com.previsao.model.Permissao.GERENTEFILIAL and  u.empresa.codigo=:codigo"),
		@NamedQuery(name = Usuario.CARREGARENCARGERENTEREGIONAL, query = "select u from Usuario u where u.permissao=br.com.previsao.model.Permissao.GERENTEREGIONAL and  u.empresa.codigo=:codigo"),
		@NamedQuery(name = Usuario.POREMAIL, query = "select u from Usuario u where lower(u.email) = :email"), })
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static String CARREGARDIRETOR = "Usuario.gerente";
	public final static String CARREGARGERENTEFILIAL = "Usuario.tecnico";
	public final static String CARREGARENCARGERENTEREGIONAL = "Usuario.carregarencarregado";
	
	
	public final static String POREMAIL = "usuario.buscaPorEmail";

	private Long codigo;

	private String email;

	private String nome;
	private Permissao permissao;
	private Usuario chefe;
	private Empresa empresa;
	private List<Usuario> subordinados = new ArrayList<>();

	private Integer Version;

	@Version
	public Integer getVersion() {
		return Version;
	}

	public void setVersion(Integer version) {
		Version = version;
	}

	private String senha;

	private Telefone telafone;

	@Id
	@GeneratedValue
	@Column(name = "codigo")
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@NotBlank
	// Não pode estar em Branco
	@Email
	// Tem que ser um email válido
	@Size(max = 60)
	// Tamanho máximo de 60
	@Column(name = "email", unique = true, length = 60, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Embedded
	public Telefone getTelefone() {
		return telafone;
	}

	public void setTelefone(Telefone telafone) {
		this.telafone = telafone;
	}

	@NotBlank
	@Column(name = "nome", length = 60)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome == null ? null : nome.toUpperCase().trim();
	}

	@Column(name = "permissao", length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_chefe")
	public Usuario getChefe() {
		return chefe;
	}

	public void setChefe(Usuario chefe) {
		this.chefe = chefe;
	}

	@OneToMany(mappedBy = "chefe", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	public List<Usuario> getSubordinados() {
		return subordinados;
	}

	public void setSubordinados(List<Usuario> subordinados) {
		this.subordinados = subordinados;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_empresa")
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@NotBlank
	// Não pode estar em branco
	@Column(nullable = false)
	// não pode ser nulo, aceita até 15 caracteres
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}