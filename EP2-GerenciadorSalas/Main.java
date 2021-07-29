import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws IOException {
        // String responsavel = "felipeoes@usp.br";
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y");
        List<String> participantes = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LocalDate dataInicial;
        LocalDate dataFinal;

        System.out.println(
                "Olá! Digite o período em que deseja marcar a reunião. Por exemplo, a entrada para um período entre 10 e 24 de abril de 2021 deve ser 10,24,8,2021");
        while (true) {

            String[] entrada = reader.readLine().split(",");
            int diaInicial = Integer.parseInt(entrada[0]);
            int diaFinal = Integer.parseInt(entrada[1]);
            int mes = Integer.parseInt(entrada[2]);
            int ano = Integer.parseInt(entrada[3]);

            try {
                dataInicial = LocalDate.of(ano, mes, diaInicial); // periodo
                dataFinal = LocalDate.of(ano, mes, diaFinal);

                break;
            } catch (Exception e) {
                System.out.println(
                        "Por favor, digite uma data válida. Exemplo: 5,10,11,2021 \nSendo que 5 representa o dia inicial do período, 10 representa o dia final, 11 é o mês novembro e 2021 é o ano");
            }
        }

        System.out.println(
                "Agora digite os emails de todos os participantes da reunião, um por vez seguido de um \"Enter\" \nQuando finalizar, digite \"X\" e aperte \"Enter\"");
        String entrada = reader.readLine();
        while (!entrada.equals("X")) {
            participantes.add(entrada);
            entrada = reader.readLine();
        }
        // participantes.add(responsavel); verificar se é necessário adicionar
        // responsável na lista de participantes dps

        MarcadorDeReuniao marc = new MarcadorDeReuniao();
        // GerenciadorDeSalas ger = new GerenciadorDeSalas();

        marc.marcarReuniaoEntre(dataInicial, dataFinal, participantes);
        HashMap<LocalDateTime, LocalDateTime> relHorarios = new HashMap<>();
        HashMap<String, HashMap<LocalDateTime, LocalDateTime>> relDisponibilidade = new HashMap<>();

        for (String participante : participantes) {
            System.out.println(participante
                    + " Por favor, indique seus horários inicial e final de disponibilidade para a reunião, respeitando o período estipulado. Sendo que o inicial deve ser separado do final por um hífen (-). Ex: 27/08/2021 14:25-27/08/2021 18:00");
            System.out.println("Data inicial: " + formatador.format(dataInicial) + "\nData final: "
                    + formatador.format(dataFinal));

            String[] horarios = reader.readLine().split("-");
            while (!horarios[0].equals("X")) {
                // String[] horarios = reader.readLine().split("-");
                String[] datasIni = horarios[0].strip().split("/");
                String[] horariosIni = datasIni[2].split(" ");
                String[] temposIni = horariosIni[1].split(":");

                String[] datasFim = horarios[1].strip().split("/");
                String[] horariosFim = datasFim[2].split(" ");
                String[] temposFim = horariosFim[1].split(":");

                try {
                    LocalDateTime inicio = LocalDateTime.of(Integer.parseInt(horariosIni[0]),
                            Integer.parseInt(datasIni[1]), Integer.parseInt(datasIni[0]),
                            Integer.parseInt(temposIni[0]), Integer.parseInt(temposIni[1]));
                    LocalDateTime fim = LocalDateTime.of(Integer.parseInt(horariosFim[0]),
                            Integer.parseInt(datasFim[1]), Integer.parseInt(datasFim[0]),
                            Integer.parseInt(temposFim[0]), Integer.parseInt(temposFim[1]));

                    relHorarios.put(inicio, fim);
                } catch (Exception e) {
                    System.out
                            .println("Por favor, digite um horário válido. Exemplo: 27/08/2021 14:25-27/08/2021 18:00");
                }
                System.out.println(
                        "Caso deseje adicionar adicionar mais algum horário de disponibilidade, basta digitá-lo. Caso tenha finalizado, digite \"X\" e aperte Enter");
                horarios = reader.readLine().split("-");
            }
            relDisponibilidade.put(participante, relHorarios);
            relHorarios = new HashMap<>();
        }

        for (Entry<String, HashMap<LocalDateTime, LocalDateTime>> participante : relDisponibilidade.entrySet()) {
            HashMap<LocalDateTime, LocalDateTime> horarios = participante.getValue();

            for (Entry<LocalDateTime, LocalDateTime> horario : horarios.entrySet()) {
                marc.indicaDisponibilidadeDe(participante.getKey(), horario.getKey(), horario.getValue());
            }
            marc.relDatas = new ArrayList<HashMap<LocalDateTime, LocalDateTime>>();
        }

        marc.mostraSobreposicao();
    }
}
