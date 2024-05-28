/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.kubeforce.payroll.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.cometbid.kubeforce.payroll.gson.util.Exclude;
import org.cometbid.kubeforce.payroll.common.util.ArtifactForFramework;
import org.springframework.data.annotation.Version;

/**
 *
 * @author samueladebowale
 * @param <T>
 */
@MappedSuperclass
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractEntity<T extends EntityId> implements Entity<T>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6425982031170127365L;

    @Id
    @JsonIgnore
    @Exclude
    protected T id;

    @Version
    @JsonIgnore
    @Exclude
    protected int version;

    @ArtifactForFramework
    protected AbstractEntity() {
    }

    protected AbstractEntity(T id) {
        this.id = id;
    }

    @Override
    public T getId() {
        return id;
    }

    protected abstract void setId();

}
