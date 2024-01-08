package com.andrelgirao.msavaliadorcreditosample.infra.clients;

import com.andrelgirao.msavaliadorcreditosample.application.domain.model.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "msclients-sample", path = "/clientes")
public interface ClientResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<Cliente> dadosCliente(@RequestParam("cpf") String cpf);

}
