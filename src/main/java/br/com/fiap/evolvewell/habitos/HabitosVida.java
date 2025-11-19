package br.com.fiap.evolvewell.habitos;

import jakarta.persistence.Embeddable;

@Embeddable
public class HabitosVida {

    private Integer horasSono;
    private Integer minutosExercicioSemana;
    private Integer pausasTurno;
    private Integer nivelEstresseAuto;

    public HabitosVida() {
    }

    public HabitosVida(Integer horasSono,
                       Integer minutosExercicioSemana,
                       Integer pausasTurno,
                       Integer nivelEstresseAuto) {
        this.horasSono = horasSono;
        this.minutosExercicioSemana = minutosExercicioSemana;
        this.pausasTurno = pausasTurno;
        this.nivelEstresseAuto = nivelEstresseAuto;
    }

    public HabitosVida(DadosHabitosVida dados) {
        this.horasSono = dados.horasSono();
        this.minutosExercicioSemana = dados.minutosExercicioSemana();
        this.pausasTurno = dados.pausasTurno();
        this.nivelEstresseAuto = dados.nivelEstresseAuto();
    }

    public void atualizarInformacoes(DadosHabitosVida dados) {
        if (dados.horasSono() != null) {
            this.horasSono = dados.horasSono();
        }
        if (dados.minutosExercicioSemana() != null) {
            this.minutosExercicioSemana = dados.minutosExercicioSemana();
        }
        if (dados.pausasTurno() != null) {
            this.pausasTurno = dados.pausasTurno();
        }
        if (dados.nivelEstresseAuto() != null) {
            this.nivelEstresseAuto = dados.nivelEstresseAuto();
        }
    }

    public Integer getHorasSono() {
        return horasSono;
    }

    public Integer getMinutosExercicioSemana() {
        return minutosExercicioSemana;
    }

    public Integer getPausasTurno() {
        return pausasTurno;
    }

    public Integer getNivelEstresseAuto() {
        return nivelEstresseAuto;
    }
}
