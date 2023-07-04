package hr.rba.creditcardservice.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity(name = "people")
@Getter
@Setter
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long personId;
    private String firstName;
    private String lastName;
    private String oib;

    @Enumerated(value = EnumType.STRING)
    private PersonStatus status;

    @OneToMany(mappedBy = "person")
    private Set<FileEntity> files;
}
