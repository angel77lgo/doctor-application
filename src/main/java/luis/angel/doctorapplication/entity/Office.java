package luis.angel.doctorapplication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String floor;
    @Column(name = "floor_number")
    private String floorNumber;
}
