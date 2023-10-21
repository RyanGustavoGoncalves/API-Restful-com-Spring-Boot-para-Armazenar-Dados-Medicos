package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.endereco.Endereco;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    @Autowired //Instanciando o repository
    private MedicoRepository repository;

    @PostMapping
    @Transactional//Conversar com o banco de dados
    public void cadastrar(@RequestBody @Valid /*Integrando o Spring com o bin validation*/ DadosCadastroMedico dados){ //Padrão DTO
        repository.save(new Medico(dados));
    }
    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){ //Fazendo paginação com spring
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new) ;
        //Convertendo list de medico para DadosListagemMedico
        //findAll vem de JPARepository
    }
    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedicos dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }
    @DeleteMapping("/{id}")//parâmetro dinâmico
    @Transactional
    public void excluir(@PathVariable /*declarando que é a mesma variavel da url*/ Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
    }
}
