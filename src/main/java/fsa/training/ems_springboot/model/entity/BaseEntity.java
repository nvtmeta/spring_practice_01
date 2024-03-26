package fsa.training.ems_springboot.model.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
//MapperSuperClass for ex deleted as field in table
public abstract class BaseEntity {

    private boolean deleted = false;

}
