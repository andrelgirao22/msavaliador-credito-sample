package com.andrelgirao.msavaliadorcreditosample.application;

import com.andrelgirao.msavaliadorcreditosample.application.domain.model.Cliente;
import com.andrelgirao.msavaliadorcreditosample.application.domain.model.SituacaoCliente;
import com.andrelgirao.msavaliadorcreditosample.infra.clients.ClientResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClientResourceClient clientResourceClient;

    public SituacaoCliente getSituacaoCliente(String cpf) {
        //obter dados do cliente msclientes
        //obter dados dos cartoes mscartoes

        ResponseEntity<Cliente> dadosClienteResponse = clientResourceClient.dadosCliente(cpf);

        return SituacaoCliente.builder()
                .cliente(dadosClienteResponse.getBody())
                .build();
    }
}
