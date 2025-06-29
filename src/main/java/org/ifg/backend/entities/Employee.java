package org.ifg.backend.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
public class Employee extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "first_name", length = 50, nullable = false)
    public String firstName;

    @Column(name = "last_name", length = 150)
    public String lastName;

    @ManyToOne
    @JoinColumn(name = "workplace_name", referencedColumnName = "company_name")
    public Company workplace;
}
