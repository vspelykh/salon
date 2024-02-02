package ua.vspelykh.schedulemicroservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "master_levels")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MasterLevel extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "about_id", referencedColumnName = "id")
    private Translation about;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
