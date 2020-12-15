package mx.gob.queretaro.repositories.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the TD_RECURSOS_ROLES database table.
 *
 */
@Embeddable
public class TdRecursoRolPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_RECURSO", insertable = false, updatable = false, unique = true, nullable = false)
	private long idRecurso;

	@Column(name = "ID_ROL", insertable = false, updatable = false, unique = true, nullable = false)
	private long idRol;

	public TdRecursoRolPK() {
	}

	public TdRecursoRolPK(long idRecurso, long idRol) {
		this.idRecurso = idRecurso;
		this.idRol = idRol;
	}

	public long getIdRecurso() {
		return this.idRecurso;
	}

	public void setIdRecurso(long idRecurso) {
		this.idRecurso = idRecurso;
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
		if (!(other instanceof TdRecursoRolPK)) {
			return false;
		}
		TdRecursoRolPK castOther = (TdRecursoRolPK) other;
		return (this.idRecurso == castOther.idRecurso) && (this.idRol == castOther.idRol);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idRecurso ^ (this.idRecurso >>> 32)));
		hash = hash * prime + ((int) (this.idRol ^ (this.idRol >>> 32)));

		return hash;
	}
}