package dbp.hackathon.Estudiante;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dbp.hackathon.Ticket.Ticket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private Long studentCode;

    @JsonBackReference
    @OneToMany(mappedBy = "estudiante")
    private List<Ticket> tickets;
}
