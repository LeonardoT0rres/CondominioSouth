package com.easyvoteapi.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
