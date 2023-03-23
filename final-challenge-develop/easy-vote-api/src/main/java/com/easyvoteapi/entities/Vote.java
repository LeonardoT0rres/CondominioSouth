package com.easyvoteapi.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "schedule_id")
    private Long scheduleId;
    private Boolean vote;
}
