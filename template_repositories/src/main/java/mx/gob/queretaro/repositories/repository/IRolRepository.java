package mx.gob.queretaro.repositories.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.gob.queretaro.repositories.model.TcRecurso;
import mx.gob.queretaro.repositories.model.TcRol;

public interface IRolRepository extends JpaRepository<TcRol, Serializable> {

	@Query(
			value = "SELECT NEW TcRol(r.id, r.rol, r.descripcion, r.usuarioI, r.fechaI, r.estatus) FROM TcRol r WHERE r.estatus = 'AC' AND (UPPER(r.rol) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(r.descripcion) LIKE CONCAT('%',UPPER(:search),'%'))",
			countQuery = "SELECT COUNT(r.id) FROM TcRol r WHERE r.estatus = 'AC' AND (UPPER(r.rol) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(r.descripcion) LIKE CONCAT('%',UPPER(:search),'%'))"
			)
	Page<TcRol> obtenerRoles(@Param("search") String search, Pageable pageable);

	@Query("SELECT NEW TcRecurso(rr.tcRecurso.id, rr.tcRecurso.recurso, rr.tcRecurso.descripcion, rr.tcRecurso.usuarioI, rr.tcRecurso.fechaI, rr.tcRecurso.estatus) FROM TdRecursoRol rr WHERE rr.tcRol.id = :idRol AND rr.tcRecurso.estatus  = :estatus")
	List<TcRecurso> obtenerRecursosPorIdRol(@Param("idRol") long idRol, @Param("estatus") String estatus);

	@Modifying
	@Query("UPDATE TcRol r SET r.estatus = 'IN' WHERE r.id = :idRol")
	void borrarRol(@Param("idRol") long idRol);

	@Modifying
	@Query("DELETE FROM TdUsuarioRol ur WHERE ur.tcRol.id = :idRol")
	void borrarUsuarioRol(@Param("idRol") long idRol);

	@Modifying
	@Query("DELETE FROM TdUsuarioRol ur WHERE ur.tcRol.id = :idRol")
	void borrarRecursoRol(@Param("idRol") long idRol);

	@Query("SELECT NEW TcRol(r.id, r.rol, r.descripcion, r.usuarioI, r.fechaI, r.estatus) FROM TcRol r WHERE r.rol = :rol AND r.estatus = :estatus")
	TcRol obtenerRolPorRol(@Param("rol") String rol, @Param("estatus") String estatus);

	@Query("SELECT NEW TcRol(r.id, r.rol, r.descripcion, r.usuarioI, r.fechaI, r.estatus) FROM TcRol r WHERE UPPER(r.rol) LIKE UPPER(CONCAT('%',:rol,'%')) AND r.estatus = :estatus ORDER BY r.rol ASC")
	List<TcRol> obtenerRolesPorRol(@Param("rol") String rol, @Param("estatus") String estatus);

	@Query("SELECT NEW TcRol(r.id, r.rol, r.descripcion, r.usuarioI, r.fechaI, r.estatus) FROM TcRol r WHERE r.estatus = :estatus")
	List<TcRol> obtenerRoles(@Param("estatus") String estatus);

}
