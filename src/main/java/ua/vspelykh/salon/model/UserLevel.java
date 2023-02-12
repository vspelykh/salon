package ua.vspelykh.salon.model;

import lombok.*;

import java.io.Serializable;

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
