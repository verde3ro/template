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
 * The persistent class for the TC_RECURSOS database table.
 *
 */
@Entity
@Table(name = "TC_RECURSOS", schema = "MGR_MUNICIPIOS")
@NamedQuery(name = "TcRecurso.findAll", query = "SELECT r FROM TcRecurso r")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TcRecurso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RECU")
	@SequenceGenerator(name = "SEQ_RECU", sequenceName = "SEQ_RECU", schema = "MGR_MUNICIPIOS", allocationSize = 1)
	private long id;

	@Column(length = 250)
	private String descripcion;

	@Column(length = 5)
	private String estatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_I")
	private Date fechaI;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_U")
	private Date fechaU;

	@Column(length = 250)
	private String recurso;

	@Column(name = "USUARIO_I", length = 20)
	private String usuarioI;

	@Column(name = "USUARIO_U", length = 20)
	private String usuarioU;

	@Column(nullable=false, precision=1)
	private long sesion;

	// bi-directional many-to-one association to TdRecursoRol
	@OneToMany(mappedBy = "tcRecurso")
	private List<TdRecursoRol> tdRecursoRol;

	public TcRecurso() {
	}

	public TcRecurso(long id, String recurso, String descripcion, String usuarioI, Date fechaI, String estatus) {
		this.id = id;
		this.recurso = recurso;
		this.descripcion = descripcion;
		this.usuarioI = usuarioI;
		this.fechaI = fechaI;
		this.estatus = estatus;
	}

	public TcRecurso(long id, String recurso, String descripcion, String usuarioI, Date fechaI, String estatus, long sesion) {
		this.id = id;
		this.recurso = recurso;
		this.descripcion = descripcion;
		this.usuarioI = usuarioI;
		this.fechaI = fechaI;
		this.estatus = estatus;
		this.sesion = sesion;
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

	public String getRecurso() {
		return this.recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
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

	public long getSesion() {
		return this.sesion;
	}

	public void setSesion(long sesion) {
		this.sesion = sesion;
	}

	public List<TdRecursoRol> getTdRecursoRol() {
		return this.tdRecursoRol;
	}

	public void setTdRecursoRol(List<TdRecursoRol> tdRecursoRol) {
		this.tdRecursoRol = tdRecursoRol;
	}

	public TdRecursoRol addRecursosRolesD(TdRecursoRol tdrecursosRoles) {
		getTdRecursoRol().add(tdrecursosRoles);
		tdrecursosRoles.setTcRecurso(this);

		return tdrecursosRoles;
	}

	public TdRecursoRol removeRecursosRolesD(TdRecursoRol tdrecursosRoles) {
		getTdRecursoRol().remove(tdrecursosRoles);
		tdrecursosRoles.setTcRecurso(null);

		return tdrecursosRoles;
	}

}