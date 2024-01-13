package com.andrelgirao.msavaliadorcreditosample.infra.mqueue;

import com.andrelgirao.msavaliadorcreditosample.application.domain.model.DadosSolicitacaoEmissaoCartao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissaoCartoes;

    public void solicitarCartao(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        var json = convertInfoJson(dados);
        rabbitTemplate.convertAndSend(queueEmissaoCartoes.getName(), json);
    }

    private String convertInfoJson(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(dados);
        return json;
    }

}
