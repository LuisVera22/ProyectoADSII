package pe.com.project.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sale_detail")
public class SaleDetailEntity {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idDetail;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "fk_sale", nullable = false)
    private SaleEntity saleEntity;

    @ManyToOne
    @JoinColumn(name = "fk_product", nullable = false)
    private ProductEntity productEntity;
}
