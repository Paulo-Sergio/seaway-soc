package br.com.seaway.SOC_Web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cores01")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cor01 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referencia;
    private String codigoCor;
    private String nomeCor;
    private String venda;
    private String venda10Dias;
    private String estoque;

    // novos campos
    private String vendaEcommerce; // 4 digitos
    private String venda10DiasEcommerce; // 4 digitos
    private String estoqueEcommerce; // 4 digitos
    // fim novos campos

    private String indice;
    private String iop;
    private String classe;
}