package mx.gob.queretaro.repositories.repository;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.gob.queretaro.repositories.model.TcParametro;


public interface IParametroRepository extends JpaRepository<TcParametro, Serializable> {

	@Query("SELECT p FROM TcParametro p WHERE p.nombre = :nombre AND p.estatus = :estatus")
	TcParametro obtenerPorNombre(@Param("nombre") String nombre, @Param("estatus") String estatus);

	@Query(
			value = "SELECT NEW TcParametro(p.id, p.nombre, p.valor, p.usuarioI, p.fechaI, p.estatus) FROM TcParametro p WHERE p.estatus = 'AC' AND (UPPER(p.nombre) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(p.valor) LIKE CONCAT('%',UPPER(:search),'%'))",
			countQuery = "SELECT COUNT(p.id) FROM TcParametro p WHERE p.estatus = 'AC' AND (UPPER(p.nombre) LIKE CONCAT('%',UPPER(:search),'%') OR UPPER(p.valor) LIKE CONCAT('%',UPPER(:search),'%'))"
			)
	Page<TcParametro> obtenerParametros(@Param("search") String search, Pageable pageable);

	@Modifying
	@Query("UPDATE TcParametro p SET p.estatus = 'IN', p.usuarioU = :usuario, p.fechaU = :fecha WHERE p.id = :idParametro")
	void borrarParametro(@Param("idParametro") long idParametro, @Param("usuario") String usuario, @Param("fecha") Date fecha);

}
