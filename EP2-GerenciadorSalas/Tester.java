import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

public class Tester {

    public static void main(String[] args) {
        String responsavel = "felipeoes@usp.br";
        List<String> participantes = new ArrayList<String>();
        String p1 = "gustavo@usp.br";
        String p2 = "marcelina@usp.br";
        String p3 = "martha@usp.br";

        participantes.add(responsavel);
        for (int i = 1; i <= 3; i++) {
            participantes.add("p" + i);
        }

        MarcadorDeReuniao marc = new MarcadorDeReuniao();
        GerenciadorDeSalas ger = new GerenciadorDeSalas();

        LocalDate dataInicial = LocalDate.of(2021, 8, 21);
        LocalDate dataFinal = LocalDate.of(2021, 8, 30);
        marc.marcarReuniaoEntre(dataInicial, dataFinal, participantes);

        HashMap<LocalDateTime, LocalDateTime> relHorarios = new HashMap<>();
        HashMap<String, HashMap<LocalDateTime, LocalDateTime>> relDisponibilidade = new HashMap<>();
        LocalDateTime p1Ini = LocalDateTime.of(2021, 8, 22, 19, 0);
        LocalDateTime p1Fim = LocalDateTime.of(2021, 8, 22, 23, 0);
        relHorarios.put(p1Ini, p1Fim);

        p1Ini = LocalDateTime.of(2021, 8, 27, 16, 0); // participante1
        p1Fim = LocalDateTime.of(2021, 8, 27, 18, 0);
        relHorarios.put(p1Ini, p1Fim);

        relDisponibilidade.put(p1, relHorarios);
        relHorarios.clear();

        LocalDateTime p2Ini = LocalDateTime.of(2021, 8, 22, 19, 0); // participante2
        LocalDateTime p2Fim = LocalDateTime.of(2021, 8, 22, 23, 0);
        relHorarios.put(p2Ini, p2Fim);

        p2Ini = LocalDateTime.of(2021, 8, 27, 16, 0);
        p2Fim = LocalDateTime.of(2021, 8, 27, 18, 0);
        relHorarios.put(p2Ini, p2Fim);

        relDisponibilidade.put(p2, relHorarios);
        relHorarios.clear();

        LocalDateTime p3Ini = LocalDateTime.of(2021, 8, 22, 07, 0); // participante3
        LocalDateTime p3Fim = LocalDateTime.of(2021, 8, 22, 10, 0);
        relHorarios.put(p3Ini, p3Fim);

        p3Ini = LocalDateTime.of(2021, 8, 27, 16, 0);
        p3Fim = LocalDateTime.of(2021, 8, 27, 17, 0);
        relHorarios.put(p3Ini, p3Fim);

        relDisponibilidade.put(p3, relHorarios);

        for (Entry<String, HashMap<LocalDateTime, LocalDateTime>> participante : relDisponibilidade.entrySet()) {
            HashMap<LocalDateTime, LocalDateTime> horarios = participante.getValue();

            for (Entry<LocalDateTime, LocalDateTime> horario : horarios.entrySet()) {
                marc.indicaDisponibilidadeDe(participante.getKey(), horario.getKey(), horario.getValue());
            }

        }

        marc.mostraSobreposicao();

    }
}
