package br.com.fiap.evolvewell.colaborador;

import br.com.fiap.evolvewell.habitos.DadosHabitosVida;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoColaborador(

        @NotNull
        Long id,
        String telefone,
        String cargo,
        String departamento,
        ModoTrabalho modoTrabalho,
        DadosHabitosVida habitos
) {
}
