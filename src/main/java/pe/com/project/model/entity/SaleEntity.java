package pe.com.project.model.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate date;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String paymentType;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "fk_client", nullable = false)
    private ClientEntity clientEntity;

    @OneToMany(mappedBy = "saleEntity", cascade = CascadeType.ALL)
    private List<SaleDetailEntity> saleDetail;
}
