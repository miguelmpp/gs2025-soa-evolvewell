package br.com.fiap.evolvewell.integracao;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AtividadeExternaClient {

    private final RestTemplate restTemplate;

    public AtividadeExternaClient() {
        this.restTemplate = new RestTemplate();
    }

    public String buscarSugestaoAtividade() {
        try {
            // API pública que sugere uma atividade aleatória
            String url = "https://www.boredapi.com/api/activity";
            Map response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.get("activity") != null) {
                return response.get("activity").toString();
            }
        } catch (RestClientException e) {
            // Em caso de erro na API externa, devolvemos uma sugestão padrão
            return "Reserve 15 minutos para uma pausa sem telas, apenas respirar e alongar.";
        }

        return "Faça uma pequena caminhada ou alongamento para aliviar a mente.";
    }
}
