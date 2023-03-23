package com.easyvoteapi.entities;

import com.easyvoteapi.utils.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "schedule_order")
    private Integer scheduleOrder;
    @Column(name = "positive_votes")
    private Integer positiveVotes;
    @Column(name = "negative_votes")
    private Integer negativeVotes;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    private Duration duration;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "assembly_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Assembly assembly;
}
