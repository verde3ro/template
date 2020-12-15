package mx.gob.queretaro.repositories.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.gob.queretaro.repositories.model.TcRol;
import mx.gob.queretaro.repositories.model.TcUsuario;

public interface IUsuarioRepository extends JpaRepository<TcUsuario, Serializable> {

	@Query("SELECT NEW TcUsuario(u.id, u.usuario, u.nombre, u.correo, u.usuarioI, u.fechaI, u.estatus) FROM TcUsuario u WHERE UPPER(u.usuario) = UPPER(:usuario) AND u.estatus = :estatus")
	TcUsuario obtenerUsuarioPorUsuario(@Param("usuario") String usuario, @Param("estatus") String estatus);

	@Query("SELECT NEW TcUsuario(u.id, u.usuario, u.nombre, u.correo, u.usuarioI, u.fechaI, u.estatus) FROM TcUsuario u WHERE u.id = :idUsuario AND u.estatus = :estatus")
	TcUsuario obtenerUsuarioPorId(@Param("idUsuario") long idUsuario, @Param("estatus") String estatus);

	@Query("SELECT NEW TcRol(ur.tcRol.id, ur.tcRol.rol, ur.tcRol.descripcion, ur.tcRol.usuarioI, ur.tcRol.fechaI, ur.tcRol.estatus) FROM TdUsuarioRol ur WHERE ur.tcUsuario.id = :idUsuario AND ur.tcRol.estatus = :estatus")
	List<TcRol> obtenerRolesPorIdUsuario(@Param("idUsuario") long idUsuario, @Param("estatus") String estatus);

	@Query(
			value = "SELECT NEW TcUsuario(u.id, u.usuario, u.nombre, u.correo, u.usuarioI, u.fechaI, u.estatus) FROM TcUsuario u WHERE u.estatus = 'AC' AND (UPPER(u.usuario) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(u.nombre) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(u.correo) LIKE CONCAT('%',UPPER(:search),'%'))",
			countQuery = "SELECT COUNT(u.id) FROM TcUsuario u WHERE u.estatus = 'AC' AND (UPPER(u.usuario) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(u.nombre) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(u.correo) LIKE CONCAT('%',UPPER(:search),'%'))"
	)
	Page<TcUsuario> obtenerUsuarios(@Param("search") String search, Pageable pageable);

	@Query("SELECT NEW TcUsuario(u.id) FROM TcUsuario u WHERE u.usuario = :usuario")
	TcUsuario obtenerUsuarioPorNombre(@Param("usuario") String usuario);

	@Modifying
	@Query("UPDATE TcUsuario u SET u.estatus = 'IN' WHERE u.id = :idUsuario")
	void borrarUsuario(@Param("idUsuario") long idUsuario);

	@Modifying
	@Query("DELETE FROM TdUsuarioRol ur WHERE ur.tcUsuario.id = :idUsuario")
	void borrarUsuarioRol(@Param("idUsuario") long idUsuario);

}
