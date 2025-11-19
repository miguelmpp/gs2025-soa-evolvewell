package br.com.fiap.evolvewell.service;

import br.com.fiap.evolvewell.colaborador.Colaborador;
import br.com.fiap.evolvewell.colaborador.ModoTrabalho;
import br.com.fiap.evolvewell.colaborador.PlanoBemEstarResponse;
import br.com.fiap.evolvewell.habitos.HabitosVida;
import br.com.fiap.evolvewell.integracao.AtividadeExternaClient;
import org.springframework.stereotype.Service;

@Service
public class PlanoBemEstarService {

    private final AtividadeExternaClient atividadeExternaClient;

    public PlanoBemEstarService(AtividadeExternaClient atividadeExternaClient) {
        this.atividadeExternaClient = atividadeExternaClient;
    }

    public PlanoBemEstarResponse gerarPlano(Colaborador colaborador) {
        HabitosVida h = colaborador.getHabitos();

        double score = calcularIndice(h);
        String nivelRisco = classificarRisco(score);
        String recomendacao = montarRecomendacao(colaborador.getModoTrabalho(), score);
        String sugestaoExterna = atividadeExternaClient.buscarSugestaoAtividade();

        return new PlanoBemEstarResponse(
                colaborador.getId(),
                colaborador.getNome(),
                colaborador.getModoTrabalho(),
                score,
                nivelRisco,
                recomendacao,
                sugestaoExterna
        );
    }

    private double calcularIndice(HabitosVida h) {
        if (h == null) return 5.0; // neutro se não tiver dados

        double score = 0.0;

        // Sono
        if (h.getHorasSono() == null) {
            score += 1;
        } else if (h.getHorasSono() >= 7 && h.getHorasSono() <= 9) {
            score += 3;
        } else if (h.getHorasSono() >= 6) {
            score += 2;
        } else {
            score += 1;
        }

        // Exercício
        if (h.getMinutosExercicioSemana() == null) {
            score += 1;
        } else if (h.getMinutosExercicioSemana() >= 150) {
            score += 3;
        } else if (h.getMinutosExercicioSemana() >= 60) {
            score += 2;
        } else {
            score += 1;
        }

        // Pausas
        if (h.getPausasTurno() == null) {
            score += 1;
        } else if (h.getPausasTurno() >= 3) {
            score += 3;
        } else if (h.getPausasTurno() >= 1) {
            score += 2;
        } else {
            score += 0;
        }

        // Estresse (quanto menor, melhor)
        if (h.getNivelEstresseAuto() == null) {
            score += 1;
        } else if (h.getNivelEstresseAuto() <= 4) {
            score += 3;
        } else if (h.getNivelEstresseAuto() <= 7) {
            score += 2;
        } else {
            score += 1;
        }

        // Score máximo aqui é 12. Vamos normalizar para uma escala de 0 a 10.
        double indice = (score / 12.0) * 10.0;
        return Math.round(indice * 10.0) / 10.0; // uma casa decimal
    }

    private String classificarRisco(double indice) {
        if (indice >= 7.5) {
            return "Baixo";
        } else if (indice >= 5.0) {
            return "Médio";
        } else {
            return "Alto";
        }
    }

    private String montarRecomendacao(ModoTrabalho modoTrabalho, double indice) {
        String base;
        if (indice >= 7.5) {
            base = "Manter a rotina atual, garantindo pausas e preservando a qualidade do sono.";
        } else if (indice >= 5.0) {
            base = "Ajustar alguns hábitos: reforçar pausas durante o expediente e organizar melhor horários de descanso.";
        } else {
            base = "Priorizar recuperação: evitar excesso de horas extras, cuidar do sono e, se possível, buscar apoio profissional.";
        }

        String complemento;
        if (modoTrabalho == ModoTrabalho.REMOTO) {
            complemento = " Como trabalha remoto, é importante definir limites claros entre casa e trabalho.";
        } else if (modoTrabalho == ModoTrabalho.HIBRIDO) {
            complemento = " No modelo híbrido, aproveite os dias presenciais para interação e os remotos para foco.";
        } else {
            complemento = " No modelo presencial, procure negociar pausas curtas ao longo do dia e evitar longas jornadas.";
        }

        return base + complemento;
    }
}
