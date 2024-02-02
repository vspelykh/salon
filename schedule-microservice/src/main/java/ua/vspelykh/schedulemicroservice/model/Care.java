package ua.vspelykh.schedulemicroservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "cares")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Care extends AbstractEntity {

    @Column(name = "master_id", nullable = false)
    private UUID masterId;

    @OneToOne
    @JoinColumn(name = "care_type_id", referencedColumnName = "id")
    private CareType careType;

    @Column(name = "continuance", nullable = false)
    private Integer continuance;
}
