package mx.gob.queretaro.repositories.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the TD_USUARIOS_ROLES database table.
 *
 */
@Embeddable
public class TdUsuarioRolPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_USUARIO", insertable = false, updatable = false, unique = true, nullable = false)
	private long idUsuario;

	@Column(name = "ID_ROL", insertable = false, updatable = false, unique = true, nullable = false)
	private long idRol;

	public TdUsuarioRolPK() {
	}

	public TdUsuarioRolPK(long idUsuario, long idRol) {
		this.idUsuario = idUsuario;
		this.idRol = idRol;
	}

	public long getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public long getIdRol() {
		return this.idRol;
	}

	public void setIdRol(long idRol) {
		this.idRol = idRol;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TdUsuarioRolPK)) {
			return false;
		}
		TdUsuarioRolPK castOther = (TdUsuarioRolPK) other;
		return (this.idUsuario == castOther.idUsuario) && (this.idRol == castOther.idRol);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idUsuario ^ (this.idUsuario >>> 32)));
		hash = hash * prime + ((int) (this.idRol ^ (this.idRol >>> 32)));

		return hash;
	}
}