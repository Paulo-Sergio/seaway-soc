package br.com.seaway.SOC_Web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cores03")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cor03 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referencia;
    private String codigoCor;
    private String nomeCor;
    private String codigoLoja;
    private String nomeLoja;
    private String tamanho;
    private String estoque;
}