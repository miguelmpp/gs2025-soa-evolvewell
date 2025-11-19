package br.com.fiap.evolvewell.colaborador;

import br.com.fiap.evolvewell.habitos.DadosHabitosVida;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroColaborador(

        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{8,15}")
        String telefone,

        @NotBlank
        String cargo,

        @NotBlank
        String departamento,

        @NotNull
        ModoTrabalho modoTrabalho,

        @NotNull
        @Valid
        DadosHabitosVida habitos
) {
}
