package com.easyvoteapi.entities;

import com.easyvoteapi.utils.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, password, email;
    private Integer apartment;
    @Column(name = "password_default")
    private Integer passwordDefault;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;
}
