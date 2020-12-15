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

public interface IRecursoRepository extends JpaRepository<TcRecurso, Serializable> {

	@Query("SELECT NEW TcRecurso(r.id, r.recurso, r.descripcion, r.usuarioI, r.fechaI, r.estatus, r.sesion) FROM TcRecurso r WHERE UPPER(r.recurso) LIKE CONCAT('%',:recurso,'%') AND r.estatus = :estatus ORDER BY r.recurso ASC")
	List<TcRecurso> obtenerRecursosPorRecurso(@Param("recurso") String recurso, @Param("estatus") String estatus);

	@Query("SELECT NEW TcRecurso(r.id, r.recurso, r.descripcion, r.usuarioI, r.fechaI, r.estatus, r.sesion) FROM TcRecurso r WHERE r.recurso = :recurso AND r.estatus = :estatus")
	TcRecurso obtenerRecursoPorRecurso(@Param("recurso") String recurso, @Param("estatus") String estatus);

	@Query("SELECT NEW TcRecurso(r.id, r.recurso, r.descripcion, r.usuarioI, r.fechaI, r.estatus, r.sesion) FROM TcRecurso r WHERE r.sesion = 0 AND r.estatus = :estatus")
	List<TcRecurso> obtenerRecursosSinSesion(@Param("estatus") String estatus);

	@Query(
			value = "SELECT NEW TcRecurso(r.id, r.recurso, r.descripcion, r.usuarioI, r.fechaI, r.estatus, r.sesion) FROM TcRecurso r WHERE r.estatus = 'AC' AND (UPPER(r.recurso) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(r.descripcion) LIKE CONCAT('%',UPPER(:search),'%'))",
			countQuery = "SELECT COUNT(r.id) FROM TcRecurso r WHERE r.estatus = 'AC' AND (UPPER(r.recurso) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(r.descripcion) LIKE CONCAT('%',UPPER(:search),'%'))"
			)
	Page<TcRecurso> obtenerRecursos(@Param("search") String search, Pageable pageable);

	@Query("SELECT NEW TcRecurso(r.id, r.recurso, r.descripcion, r.usuarioI, r.fechaI, r.estatus, r.sesion) FROM TcRecurso r WHERE r.id = :id AND r.estatus = :estatus")
	TcRecurso obtenerRecursoPorId(@Param("id") long id, @Param("estatus") String estatus);

	@Modifying
	@Query("UPDATE TcRecurso r SET r.estatus = 'IN' WHERE r.id = :idRecurso")
	void borrarRecurso(@Param("idRecurso") long idRecurso);

	@Modifying
	@Query("DELETE FROM TdRecursoRol r WHERE r.tcRecurso.id = :idRecurso")
	void borrarRecursoRol(@Param("idRecurso") long idRecurso);

}
