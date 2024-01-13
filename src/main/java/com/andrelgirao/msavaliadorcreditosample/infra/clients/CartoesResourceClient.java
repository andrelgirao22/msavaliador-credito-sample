package com.andrelgirao.msavaliadorcreditosample.infra.clients;

import com.andrelgirao.msavaliadorcreditosample.application.domain.model.Cartao;
import com.andrelgirao.msavaliadorcreditosample.application.domain.model.CartaoCliente;
import com.andrelgirao.msavaliadorcreditosample.application.domain.model.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mscartoes-sample", path = "/cartoes")
public interface CartoesResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<List<CartaoCliente>> getClientesCartoes(@RequestParam("cpf") String cpf);

    @GetMapping(params = "renda")
    ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda);

}
