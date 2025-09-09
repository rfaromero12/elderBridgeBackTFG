package es.uco.tfg.elderBridge.infrastructure.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uco.tfg.elderBridge.infrastructure.database.entity.FileEntity;

public interface FilesEntityRepositoryJPA extends  JpaRepository<FileEntity, Long>{
	
	@Query("SELECT m FROM FILE m WHERE m.creator.societyId = :societyId")
	List<FileEntity> findFilesBySociety(@Param("societyId") Long societyId);

	@Query("SELECT m FROM FILE m WHERE m.name = :name")
	Optional<FileEntity> findFileByName(@Param("name") String name);

	@Query("SELECT COUNT(m) > 0 FROM FILE m WHERE m.name =:name AND m.creator.societyId = :societyId")
	boolean existsByNameAndSocietyId(@Param("name") String name, @Param("societyId") Long societyId);
}
