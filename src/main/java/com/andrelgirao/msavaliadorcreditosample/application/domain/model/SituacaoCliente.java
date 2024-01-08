package com.andrelgirao.msavaliadorcreditosample.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SituacaoCliente {

    private Cliente cliente;
    private List<CartaoCliente> cartoes;
}
