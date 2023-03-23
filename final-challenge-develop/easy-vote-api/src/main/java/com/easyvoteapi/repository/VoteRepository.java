package com.easyvoteapi.repository;

import com.easyvoteapi.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserIdAndScheduleId(Long userId, Long scheduleId);
}
