package br.com.mvc.model.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    public static final String DISPONIVEL = "DISPONIVEL";
    public static final String INDISPONIVEL = "INDISPONIVEL";
    public static final String DESCONTINUADO = "DESCONTINUADO";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "state")
    private String state;

    @PrePersist
    public void definirEstadoInicial() {
        if (state == null || state.isBlank()) {
            state = INDISPONIVEL;
        }
    }

}
