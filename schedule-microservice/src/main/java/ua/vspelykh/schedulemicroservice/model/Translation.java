package ua.vspelykh.schedulemicroservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@Entity
@Table(name = "translations")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Translation extends AbstractEntity {

    @Column(name = "foreign_key_id", nullable = false)
    private UUID foreignKeyId;

    @Column(name = "en", nullable = false)
    private String en;

    @Column(name = "ua", nullable = false)
    private String ua;
}