package org.cometbid.kubeforce.payroll.employee;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author samueladebowale
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
