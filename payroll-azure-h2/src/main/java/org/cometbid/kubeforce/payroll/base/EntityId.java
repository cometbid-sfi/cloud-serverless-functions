/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.kubeforce.payroll.base;

import java.io.Serializable;

/**
 * Interface for primary keys of entities.
 *
 * @author samueladebowale
 *
 * @param <T> the underlying type of the entity id
 */
public interface EntityId<T> extends Serializable {

    T getId();

    String asString();
}
