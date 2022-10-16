package itmo.soa3.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@ToString(of = {"id", "name", "coordinates", "creationDate", "from", "to", "distance"})
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinate coordinates;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "\"from\"", nullable = false)
    private Location from;

    @ManyToOne
    @JoinColumn(name = "\"to\"", nullable = false)
    private Location to;

    @Column(name = "distance")
    private int distance;
}