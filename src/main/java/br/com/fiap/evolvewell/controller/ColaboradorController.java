package br.com.fiap.evolvewell.controller;

import br.com.fiap.evolvewell.colaborador.Colaborador;
import br.com.fiap.evolvewell.colaborador.ColaboradorRepository;
import br.com.fiap.evolvewell.colaborador.DadosAtualizacaoColaborador;
import br.com.fiap.evolvewell.colaborador.DadosCadastroColaborador;
import br.com.fiap.evolvewell.colaborador.DadosListagemColaborador;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import br.com.fiap.evolvewell.colaborador.PlanoBemEstarResponse;
import br.com.fiap.evolvewell.service.PlanoBemEstarService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository repository;

    @Autowired
    private PlanoBemEstarService planoBemEstarService;


    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroColaborador dados) {
        repository.save(new Colaborador(dados));
    }

    @GetMapping
    public Page<DadosListagemColaborador> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemColaborador::new);
    }

    @GetMapping("/{id}/plano-bem-estar")
    public PlanoBemEstarResponse gerarPlano(@PathVariable Long id) {
        Colaborador colaborador = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Colaborador não encontrado"
                ));

        return planoBemEstarService.gerarPlano(colaborador);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoColaborador dados) {
        Colaborador colaborador = repository.getReferenceById(dados.id());
        colaborador.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
        Colaborador colaborador = repository.getReferenceById(id);
        colaborador.excluir();
    }
}
