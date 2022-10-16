package itmo.soa3.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString(of = {"id", "x", "y"})
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "coordinates")
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "x")
    private double x;

    @Column(name = "y", nullable = false)
    private Float y;
}