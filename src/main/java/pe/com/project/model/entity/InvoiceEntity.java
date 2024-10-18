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
@Table(name = "tb_invoice")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInvoice;

    @Column(nullable = false)
    private int idRepleinshment;

    @Column(nullable = false)
    private double amountPaid;

    @Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate paymentDate;

    @Column(nullable = false)
    private String paymentType;
}
