package br.com.fiap.evolvewell.colaborador;

public record PlanoBemEstarResponse(
        Long colaboradorId,
        String nome,
        ModoTrabalho modoTrabalho,
        double indiceBemEstar,
        String nivelRisco,
        String recomendacaoGeral,
        String sugestaoAtividadeExterna
) {
}
