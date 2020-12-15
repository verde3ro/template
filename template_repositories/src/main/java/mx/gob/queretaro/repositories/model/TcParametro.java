package mx.gob.queretaro.repositories.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The persistent class for the TC_PARAMETROS database table.
 *
 */
@Entity
@Table(name = "TC_PARAMETROS", schema = "MGR_MUNICIPIOS")
@NamedQuery(name = "TcParametro.findAll", query = "SELECT p FROM TcParametro p")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TcParametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PARA")
	@SequenceGenerator(name = "SEQ_PARA", sequenceName = "SEQ_PARA", schema = "MGR_MUNICIPIOS", allocationSize = 1)
	private long id;

	@Column(nullable = false, length = 5)
	private String estatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_I", nullable = false)
	private Date fechaI;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_U")
	private Date fechaU;

	@Column(nullable = false, length = 50)
	private String nombre;

	@Column(name = "USUARIO_I", nullable = false, length = 20)
	private String usuarioI;

	@Column(name = "USUARIO_U", length = 20)
	private String usuarioU;

	@Column(nullable = false, length = 4000)
	private String valor;

	public TcParametro() {
	}

	public TcParametro(long id, String nombre, String valor, String usuarioI, Date fechaI, String estatus) {
		this.id = id;
		this.nombre = nombre;
		this.valor = valor;
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

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}