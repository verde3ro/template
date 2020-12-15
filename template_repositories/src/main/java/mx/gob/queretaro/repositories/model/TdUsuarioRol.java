package mx.gob.queretaro.repositories.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The persistent class for the TD_USUARIOS_ROLES database table.
 *
 */
@Entity
@Table(name = "TD_USUARIOS_ROLES", schema = "MGR_MUNICIPIOS")
@NamedQuery(name = "TdUsuarioRol.findAll", query = "SELECT u FROM TdUsuarioRol u")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TdUsuarioRol implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TdUsuarioRolPK id;

	@Column(nullable = false, length = 5)
	private String estatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_I", nullable = false)
	private Date fechaI;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_U")
	private Date fechaU;

	@Column(name = "USUARIO_I", nullable = false, length = 20)
	private String usuarioI;

	@Column(name = "USUARIO_U", length = 20)
	private String usuarioU;

	// bi-directional many-to-one association to TcRol
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ROL", nullable = false, insertable = false, updatable = false)
	private TcRol tcRol;

	// bi-directional many-to-one association to TcUsuario
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false, insertable = false, updatable = false)
	private TcUsuario tcUsuario;

	public TdUsuarioRol() {
	}

	public TdUsuarioRolPK getId() {
		return this.id;
	}

	public void setId(TdUsuarioRolPK id) {
		this.id = id;
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

	public TcRol getTcRole() {
		return this.tcRol;
	}

	public void setTcRol(TcRol tcRol) {
		this.tcRol = tcRol;
	}

	public TcUsuario getTcUsuario() {
		return this.tcUsuario;
	}

	public void setTcUsuario(TcUsuario tcUsuario) {
		this.tcUsuario = tcUsuario;
	}

}