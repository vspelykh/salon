package ua.vspelykh.salon.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * The AbstractBaseEntity class represents a base entity that includes common
 * fields and methods for all entities in the application.
 *
 * @version 1.0
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode
public abstract class AbstractBaseEntity implements Serializable {

    protected Integer id;

    /**
     * Creates a new instance of any entity class with a null ID.
     */
    protected AbstractBaseEntity() {
    }

    /**
     * Creates a new instance of any entity class with the specified ID.
     *
     * @param id The ID of the entity.
     */
    protected AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    /**
     * Determines whether the entity is new or not, i.e. whether it has been
     * persisted in the database yet.
     *
     * @return true if the entity is new, false otherwise.
     */
    public boolean isNew() {
        return id == null;
    }
}
