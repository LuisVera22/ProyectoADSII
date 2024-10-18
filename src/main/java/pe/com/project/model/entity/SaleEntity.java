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
@Table(name = "tb_sale")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSale;

    @Column(nullable = false, unique = true)
    private String dniClient;

    @Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate date;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentType;

    @Column(nullable = false)
    private String state;
}
