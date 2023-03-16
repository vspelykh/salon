package ua.vspelykh.salon.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * The UserLevel class represents a level of a master in the application.
 * It includes additional fields for the master's level, description and activity status.
 *
 * @version 1.0
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "masterId")
public class UserLevel implements Serializable {

    private int masterId;
    private MastersLevel level;
    private String about;
    private String aboutUa;
    private boolean isActive;

    public UserLevel(int masterId, MastersLevel level, String about, String aboutUa, boolean isActive) {
        this.masterId = masterId;
        this.level = level;
        this.about = about;
        this.aboutUa = aboutUa;
        this.isActive = isActive;
    }
}
