package com.easyvoteapi.repository;

import com.easyvoteapi.entities.Assembly;
import com.easyvoteapi.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssemblyRepository extends JpaRepository<Assembly, Long> {

    @Override
    @Query(value = "SELECT * FROM tb_assemblies " +
            "WHERE status IN ('PENDENTE','ANDAMENTO','CONCLUIDA')", nativeQuery = true)
    List<Assembly> findAll();

    Optional<Assembly> findByName(String name);

    Optional<List<Assembly>> findByStatus(Status status);

    Optional<Assembly> findByIdAndStatus(Long id, Status status);

    Optional<Assembly> findByLocaleAndStartAndStatusIsNot(String locale, LocalDateTime start, Status status);

    Optional<Assembly> findByLocaleAndStartAndStatusIsNotAndIdIsNot(String locale, LocalDateTime start, Status status, Long id);
}
