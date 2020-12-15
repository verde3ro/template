package mx.gob.queretaro.repositories.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.gob.queretaro.repositories.model.TdUsuarioRol;

public interface IUsuarioRolRepository extends JpaRepository<TdUsuarioRol, Serializable> {

	@Query("SELECT NEW TcUsuario(u.id, u.usuario, u.nombre, u.correo, u.usuarioI, u.fechaI, u.estatus) FROM TcUsuario u JOIN u.tdUsuarioRol ur JOIN ur.tcRol r WHERE r.rol = :rol AND u.estatus = :estatus")
	List<TdUsuarioRol> obtenerUsuariosPorRol(@Param("rol") String rol, @Param("estatus") String estatus);

}
