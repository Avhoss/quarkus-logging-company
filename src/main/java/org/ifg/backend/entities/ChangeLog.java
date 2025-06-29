package org.ifg.backend.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ChangeLog extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "change_type", length = 100, nullable = false)
    public String changeType;

    @Column(name = "description", length = 1000, nullable = false)
    public String description;

    @Column(name = "log_date", nullable = false)
    public LocalDateTime logDate;
}
