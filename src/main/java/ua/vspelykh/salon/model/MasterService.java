package ua.vspelykh.salon.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class MasterService extends AbstractBaseEntity {

    private int masterId;
    private int baseServiceId;
    private int continuance;

    public MasterService(int masterId, int baseServiceId, int continuance) {
        this.masterId = masterId;
        this.baseServiceId = baseServiceId;
        this.continuance = continuance;
    }

    public MasterService(Integer id, int masterId, int baseServiceId, int continuance) {
        super(id);
        this.masterId = masterId;
        this.baseServiceId = baseServiceId;
        this.continuance = continuance;
    }

}
