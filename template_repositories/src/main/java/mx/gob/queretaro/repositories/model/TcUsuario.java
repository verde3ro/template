package mx.gob.queretaro.repositories.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The persistent class for the TC_USUARIOS database table.
 *
 */
@Entity
@Table(name = "TC_USUARIOS", schema = "MGR_MUNICIPIOS")
@NamedQuery(name = "TcUsuario.findAll", query = "SELECT u FROM TcUsuario u")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TcUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUA")
	@SequenceGenerator(name = "SEQ_USUA", sequenceName = "SEQ_USUA", schema = "MGR_MUNICIPIOS", allocationSize = 1)
	private long id;

	@Column(length = 100)
	private String correo;

	@Column(nullable = false, length = 5)
	private String estatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_I", nullable = false)
	private Date fechaI;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_U")
	private Date fechaU;

	@Column(nullable = false, length = 250)
	private String nombre;

	@Column(length = 50)
	private String password;

	@Column(nullable = false, length = 20)
	private String usuario;

	@Column(name = "USUARIO_I", nullable = false, length = 20)
	private String usuarioI;

	@Column(name = "USUARIO_U", length = 20)
	private String usuarioU;

	// bi-directional many-to-one association to TdUsuarioRol
	@OneToMany(mappedBy = "tcUsuario")
	private List<TdUsuarioRol> tdUsuarioRol;

	public TcUsuario() {
	}

	public TcUsuario(long id) {
		this.id = id;
	}

	public TcUsuario(long id, String usuario, String nombre, String correo, String usuarioI, Date fechaI, String estatus) {
		this.id = id;
		this.usuario = usuario;
		this.nombre = nombre;
		this.correo = correo;
		this.usuarioI = usuarioI;
		this.fechaI = fechaI;
		this.estatus = estatus;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Date getFechaI() {
		return this.fechaI;
	}

	public void setFechaI(Date fechaI) {
		this.fechaI = fechaI;
	}

	public Date getFechaU() {
		return this.fechaU;
	}

	public void setFechaU(Date fechaU) {
		this.fechaU = fechaU;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuarioI() {
		return this.usuarioI;
	}

	public void setUsuarioI(String usuarioI) {
		this.usuarioI = usuarioI;
	}

	public String getUsuarioU() {
		return this.usuarioU;
	}

	public void setUsuarioU(String usuarioU) {
		this.usuarioU = usuarioU;
	}

	public List<TdUsuarioRol> getTdUsuarioRol() {
		return this.tdUsuarioRol;
	}

	public void setTdUsuarioRol(List<TdUsuarioRol> tdUsuarioRol) {
		this.tdUsuarioRol = tdUsuarioRol;
	}

	public TdUsuarioRol addUsuarioRolesD(TdUsuarioRol tdUsuarioRol) {
		getTdUsuarioRol().add(tdUsuarioRol);
		tdUsuarioRol.setTcUsuario(this);

		return tdUsuarioRol;
	}

	public TdUsuarioRol removeUsuarioRolesD(TdUsuarioRol tdUsuarioRol) {
		getTdUsuarioRol().remove(tdUsuarioRol);
		tdUsuarioRol.setTcUsuario(null);

		return tdUsuarioRol;
	}

}