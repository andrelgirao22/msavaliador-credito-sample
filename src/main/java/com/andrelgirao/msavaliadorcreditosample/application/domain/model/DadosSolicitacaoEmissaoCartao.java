package com.andrelgirao.msavaliadorcreditosample.application.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DadosSolicitacaoEmissaoCartao {

    private Long idcartao;
    private String cpf;
    private String endereco;
    private BigDecimal limiteLiberado;


}
