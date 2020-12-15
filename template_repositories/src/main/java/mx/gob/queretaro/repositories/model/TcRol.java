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
 * The persistent class for the TC_ROLES database table.
 *
 */
@Entity
@Table(name = "TC_ROLES", schema = "MGR_MUNICIPIOS")
@NamedQuery(name = "TcRol.findAll", query = "SELECT r FROM TcRol r")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TcRol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROLE")
	@SequenceGenerator(name = "SEQ_ROLE", sequenceName = "SEQ_ROLE", schema = "MGR_MUNICIPIOS", allocationSize = 1)
	private long id;

	@Column(nullable = false, length = 250)
	private String descripcion;

	@Column(nullable = false, length = 5)
	private String estatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_I", nullable = false)
	private Date fechaI;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_U")
	private Date fechaU;

	@Column(nullable = false, length = 20)
	private String rol;

	@Column(name = "USUARIO_I", nullable = false, length = 20)
	private String usuarioI;

	@Column(name = "USUARIO_U", length = 20)
	private String usuarioU;

	// bi-directional many-to-one association to TdRecursoRol
	@OneToMany(mappedBy = "tcRecurso")
	private List<TdRecursoRol> tdRecursoRol;

	// bi-directional many-to-one association to TdUsuarioRol
	@OneToMany(mappedBy = "tcRol")
	private List<TdUsuarioRol> tdUsuarioRol;

	public TcRol() {
	}

	public TcRol(long id, String rol, String descripcion, String usuarioI, Date fechaI, String estatus) {
		this.id = id;
		this.rol = rol;
		this.descripcion = descripcion;
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

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
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

	public List<TdRecursoRol> getTdRecursoRol() {
		return this.tdRecursoRol;
	}

	public void setTdRecursoRol(List<TdRecursoRol> tdRecursoRol) {
		this.tdRecursoRol = tdRecursoRol;
	}

	public TdRecursoRol addTdRecursoRol(TdRecursoRol tdRecursoRol) {
		getTdRecursoRol().add(tdRecursoRol);
		tdRecursoRol.setTcRol(this);

		return tdRecursoRol;
	}

	public TdRecursoRol removeTdRecursoRol(TdRecursoRol tdRecursoRol) {
		getTdRecursoRol().remove(tdRecursoRol);
		tdRecursoRol.setTcRol(null);

		return tdRecursoRol;
	}

	public List<TdUsuarioRol> getTdUsuarioRol() {
		return this.tdUsuarioRol;
	}

	public void setTdUsuarioRol(List<TdUsuarioRol> tdUsuarioRol) {
		this.tdUsuarioRol = tdUsuarioRol;
	}

	public TdUsuarioRol addTdUsuarioRol(TdUsuarioRol tdUsuarioRol) {
		getTdUsuarioRol().add(tdUsuarioRol);
		tdUsuarioRol.setTcRol(this);

		return tdUsuarioRol;
	}

	public TdUsuarioRol removeTdUsuarioRol(TdUsuarioRol tdUsuarioRol) {
		getTdUsuarioRol().remove(tdUsuarioRol);
		tdUsuarioRol.setTcRol(null);

		return tdUsuarioRol;
	}

}