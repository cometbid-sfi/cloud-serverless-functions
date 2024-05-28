/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.kubeforce.payroll.base;

import java.io.Serializable;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.cometbid.kubeforce.payroll.common.util.ArtifactForFramework;

/**
 *
 * @author samueladebowale
 * @param <T>
 */
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractEntityId<T extends Serializable> implements Serializable, EntityId<T> {

    /**
     *
     */
    private static final long serialVersionUID = 5962381739484024405L;

    private T id;

    @ArtifactForFramework
    protected AbstractEntityId() {
    }

    protected AbstractEntityId(T id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    @Override
    public T getId() {
        return id;
    }

    @Override
    public String asString() {
        return id.toString();
    }

}
