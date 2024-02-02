package ua.vspelykh.schedulemicroservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "care_types")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CareType extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "basic_price", nullable = false)
    private Integer basicPrice;

    @OneToOne
    @JoinColumn(name = "name_id", referencedColumnName = "id")
    private Translation name;
}
