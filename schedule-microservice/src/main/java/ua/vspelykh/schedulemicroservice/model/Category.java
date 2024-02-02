package ua.vspelykh.schedulemicroservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Category extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "name_id", referencedColumnName = "id")
    private Translation name;
}