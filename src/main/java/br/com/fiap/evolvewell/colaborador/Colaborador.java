package br.com.fiap.evolvewell.colaborador;

import br.com.fiap.evolvewell.habitos.HabitosVida;
import jakarta.persistence.*;

@Table(name = "colaboradores")
@Entity(name = "Colaborador")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean ativo;

    private String nome;

    private String email;

    private String telefone;

    private String cargo;

    private String departamento;

    @Enumerated(EnumType.STRING)
    private ModoTrabalho modoTrabalho;

    @Embedded
    private HabitosVida habitos;

    // Construtor padrão exigido pelo JPA
    public Colaborador() {
    }

    // Construtor completo (opcional, mas útil se precisar)
    public Colaborador(Long id,
                       Boolean ativo,
                       String nome,
                       String email,
                       String telefone,
                       String cargo,
                       String departamento,
                       ModoTrabalho modoTrabalho,
                       HabitosVida habitos) {
        this.id = id;
        this.ativo = ativo;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cargo = cargo;
        this.departamento = departamento;
        this.modoTrabalho = modoTrabalho;
        this.habitos = habitos;
    }

    // Construtor usado no cadastro (DTO)
    public Colaborador(DadosCadastroColaborador dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cargo = dados.cargo();
        this.departamento = dados.departamento();
        this.modoTrabalho = dados.modoTrabalho();
        this.habitos = new HabitosVida(dados.habitos());
    }

    public void atualizarInformacoes(DadosAtualizacaoColaborador dados) {
        if (dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if (dados.cargo() != null) {
            this.cargo = dados.cargo();
        }
        if (dados.departamento() != null) {
            this.departamento = dados.departamento();
        }
        if (dados.modoTrabalho() != null) {
            this.modoTrabalho = dados.modoTrabalho();
        }
        if (dados.habitos() != null) {
            this.habitos.atualizarInformacoes(dados.habitos());
        }
    }

    public void excluir() {
        this.ativo = false;
    }

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCargo() {
        return cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public ModoTrabalho getModoTrabalho() {
        return modoTrabalho;
    }

    public HabitosVida getHabitos() {
        return habitos;
    }
}
