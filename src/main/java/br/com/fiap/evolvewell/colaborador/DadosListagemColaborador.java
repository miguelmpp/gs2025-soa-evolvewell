package br.com.fiap.evolvewell.colaborador;

public record DadosListagemColaborador(
        Long id,
        String nome,
        String email,
        String departamento,
        ModoTrabalho modoTrabalho
) {
    public DadosListagemColaborador(Colaborador colaborador) {
        this(
                colaborador.getId(),
                colaborador.getNome(),
                colaborador.getEmail(),
                colaborador.getDepartamento(),
                colaborador.getModoTrabalho()
        );
    }
}
