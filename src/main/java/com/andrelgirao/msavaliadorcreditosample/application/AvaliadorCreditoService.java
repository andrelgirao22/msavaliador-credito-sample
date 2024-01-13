package com.andrelgirao.msavaliadorcreditosample.application;

import com.andrelgirao.msavaliadorcreditosample.application.domain.model.*;
import com.andrelgirao.msavaliadorcreditosample.application.ex.DadosClienteNotFoundException;
import com.andrelgirao.msavaliadorcreditosample.application.ex.ErroComunicacaoMicroServicesException;
import com.andrelgirao.msavaliadorcreditosample.application.ex.ErroSolicitacaoCartaoException;
import com.andrelgirao.msavaliadorcreditosample.infra.clients.CartoesResourceClient;
import com.andrelgirao.msavaliadorcreditosample.infra.clients.ClientResourceClient;
import com.andrelgirao.msavaliadorcreditosample.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClientResourceClient clientResourceClient;
    private final CartoesResourceClient cartoesResourceClient;
    private final SolicitacaoEmissaoCartaoPublisher publisher;

    public SituacaoCliente getSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroServicesException {

        try {
            ResponseEntity<Cliente> dadosClienteResponse = clientResourceClient.dadosCliente(cpf);

            ResponseEntity<List<CartaoCliente>> dadosCartaoClienteResponse =
                    cartoesResourceClient.getClientesCartoes(cpf);

            return SituacaoCliente.builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(dadosCartaoClienteResponse.getBody())
                    .build();

        } catch (FeignException.FeignClientException e) {
            var status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw  new ErroComunicacaoMicroServicesException(e.getMessage(), e.status());
        }

    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroServicesException {
        try {
            ResponseEntity<Cliente> dadosClienteResponse = clientResourceClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesResourceClient.getCartoesRendaAte(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovados = cartoes.stream().map(cartao -> {

                var idade = dadosClienteResponse.getBody().getIdade();
                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(idade);
                var fator = idadeBD.divide(BigDecimal.TEN);
                var limiteAprovado = fator.multiply(limiteBasico);

               CartaoAprovado aprovado = new CartaoAprovado();
               aprovado.setCartao(cartao.getNome());
               aprovado.setBandeira(cartao.getBandeira());
               aprovado.setLimiteAprovado(limiteAprovado);

               return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        } catch (FeignException.FeignClientException e) {
            var status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroServicesException(e.getMessage(), e.status());
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {
        try {
            publisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }

}
