package com.easyvoteapi.repository;

import com.easyvoteapi.entities.Schedule;
import com.easyvoteapi.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByName(String name);

    Optional<Schedule> findByStatus(Status status);

    @Query(value = "SELECT * FROM tb_schedules " +
            "WHERE status IN ('PENDENTE','ANDAMENTO','DEFERIDO','INDEFERIDO') " +
            "AND assembly_id = :assemblyId", nativeQuery = true)
    List<Schedule> findAllByAssemblyId(Long assemblyId);

    @Query(value = "SELECT * FROM tb_schedules " +
            "WHERE assembly_id = :assemblyId " +
            "AND (name = :name OR schedule_order = :scheduleOrder) " +
            "AND status = :status", nativeQuery = true)
    Optional<Schedule> findByAssemblyIdAndNameOrScheduleOrderAndStatus(Long assemblyId, String name, Integer scheduleOrder, String status);

    @Query(value = "SELECT * FROM tb_schedules " +
            "WHERE assembly_id = :assemblyId " +
            "AND (name = :name OR schedule_order = :scheduleOrder) " +
            "AND status = :status " +
            "AND id <> :id", nativeQuery = true)
    Optional<Schedule> findByAssemblyIdAndNameOrScheduleOrderAndStatusAndIdIsNot(Long assemblyId, String name, Integer scheduleOrder, String status, Long id);
}
