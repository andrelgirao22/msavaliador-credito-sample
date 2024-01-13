package com.andrelgirao.msavaliadorcreditosample.application;

import com.andrelgirao.msavaliadorcreditosample.application.domain.model.DadosAvaliacao;
import com.andrelgirao.msavaliadorcreditosample.application.domain.model.DadosSolicitacaoEmissaoCartao;
import com.andrelgirao.msavaliadorcreditosample.application.domain.model.ProtocoloSolicitacaoCartao;
import com.andrelgirao.msavaliadorcreditosample.application.domain.model.SituacaoCliente;
import com.andrelgirao.msavaliadorcreditosample.application.ex.DadosClienteNotFoundException;
import com.andrelgirao.msavaliadorcreditosample.application.ex.ErroComunicacaoMicroServicesException;
import com.andrelgirao.msavaliadorcreditosample.application.ex.ErroSolicitacaoCartaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliacaoCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        SituacaoCliente situacaoCliente = null;
        try {
            situacaoCliente = avaliadorCreditoService.getSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroServicesException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus()))
                    .body(e.getMessage());
        }

    }


    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao avaliacao) {
        try {
            var retorno = avaliadorCreditoService.realizarAvaliacao(avaliacao.getCpf(), avaliacao.getRenda());
            return ResponseEntity.ok(retorno);
        } catch (DadosClienteNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroServicesException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus()))
                    .body(e.getMessage());
        }
    }
    @PostMapping("solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
        try {
            var protocoloSolicitacao = avaliadorCreditoService.solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacao);
        } catch (ErroSolicitacaoCartaoException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
