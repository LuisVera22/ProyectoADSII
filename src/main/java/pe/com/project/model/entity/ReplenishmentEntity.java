package pe.com.project.model.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_replenishment")
public class ReplenishmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRepleinshment;

    @Column(nullable = false, unique = true)
    private String rucSupplier;

    @Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate applicationDate;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String state;
}
