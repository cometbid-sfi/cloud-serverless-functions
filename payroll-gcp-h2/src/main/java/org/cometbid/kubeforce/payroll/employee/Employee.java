package org.cometbid.kubeforce.payroll.employee;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author samueladebowale
 */
@Data
@Entity
@Table(name = "employee")
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(generator = "UUID")
    private Long id;

    private String name;

    @Basic
    @Column(name = "employeeid")
    private int employeeId;

    private String email;

    private String salary;

    @Builder
    public Employee(String name, int employeeIdentifier, String email, String salary) {
        this.name = name;
        this.employeeId = employeeIdentifier;
        this.email = email;
        this.salary = salary;
    }

}
