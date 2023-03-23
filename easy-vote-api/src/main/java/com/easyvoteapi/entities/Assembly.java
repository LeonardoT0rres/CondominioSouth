package com.easyvoteapi.entities;

import com.easyvoteapi.utils.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_assemblies")
public class Assembly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, obs, cardinality, locale;
    private LocalDateTime start, finish;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private List<Schedule> schedules;
}
