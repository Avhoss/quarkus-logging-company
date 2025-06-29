package org.ifg.backend.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Company extends PanacheEntityBase {

    @Id
    @Column(name = "company_name", length = 150)
    public String companyName;

    @Column(length = 50)
    public String sector;

    @Column(length = 150)
    public String location;
}
