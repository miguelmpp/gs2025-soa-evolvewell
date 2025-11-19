package br.com.fiap.evolvewell.habitos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DadosHabitosVida(

        @NotNull
        @Min(0)
        @Max(24)
        Integer horasSono,

        @NotNull
        @Min(0)
        Integer minutosExercicioSemana,

        @NotNull
        @Min(0)
        Integer pausasTurno,

        @NotNull
        @Min(1)
        @Max(10)
        Integer nivelEstresseAuto
) {
}
