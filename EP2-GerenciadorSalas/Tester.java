import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tester {
    public static void main(String[] args) throws IOException {
        // String responsavel = "felipeoes@usp.br";
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y");
        List<String> participantes = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LocalDate dataInicial;
        LocalDate dataFinal;

        System.out.println("Olá! Digite o período em que deseja marcar a reunião. Por exemplo, a entrada para um período entre 10 e 24 de abril de 2021 deve ser 10,24,8,2021");
        while(true) {
            
            String[] entrada = reader.readLine().split(",");
            int diaInicial = Integer.parseInt(entrada[0]);
            int diaFinal = Integer.parseInt(entrada[1]);
            int mes = Integer.parseInt(entrada[3]);
            int ano = Integer.parseInt(entrada[4]);
            
        
            try {
            dataInicial = LocalDate.of(ano, mes, diaInicial); // periodo
            dataFinal = LocalDate.of(ano, mes, diaFinal);
            
            break;
            }
            catch(Exception e) {
                System.out.println("Por favor, digite uma data válida. Exemplo: 5,10,11,2021 \nSendo que 5 representa o dia inicial do período, 10 representa o dia final, 11 é o mês novembro e 2021 é o ano");
            }
    }
        
        System.out.println("Agora digite os emails de todos os participantes da reunião, um por vez seguido de um \"Enter\" \nQuando finalizar, digite X e aperte \"Enter\"");
        String entrada = reader.readLine();
        while(entrada != "X") {
            participantes.add(entrada);
        }
        // String p1 = "gustavo@usp.br";
        // String p2 = "marcelina@usp.br";
        // String p3 = "martha@usp.br";

        // participantes.add(p1);
        // participantes.add(p2);
        // participantes.add(p3);
        // participantes.add(responsavel); verificar se é necessário adicionar
        // responsável na lista de participantes dps

        MarcadorDeReuniao marc = new MarcadorDeReuniao();
        // GerenciadorDeSalas ger = new GerenciadorDeSalas();

        marc.marcarReuniaoEntre(dataInicial, dataFinal, participantes);
        HashMap<LocalDateTime, LocalDateTime> relHorarios = new HashMap<>();
        HashMap<String, HashMap<LocalDateTime, LocalDateTime>> relDisponibilidade = new HashMap<>();

        for (String participante : participantes) {
            System.out.println(participante + " Por favor, indique seus horários inicial e final de disponibilidade para a reunião, respeitando o período estipulado. Sendo que o inicial deve ser separado do final por um hífen (-). Ex: 27/08/2021 14:25-27/08/2021 18:00");
            System.out.println("Data inicial: " + formatador.format(dataInicial) + "\nData final: " + formatador.format(dataFinal));
            
            while(true) {
            String[] horarios = reader.readLine().split("-");
            String[] datasIni = horarios[0].strip().split("/");
            String[] horariosIni = datasIni[2].split(" ");
            // String[] horarios
            // LocalDateTime inicial = LocalDateTime.of(Integer.parseInt(horariosIni[0]), Integer.parseInt(datasIni[1]), Integer.parseInt(datasIni[0]), Integer.parseInt(horariosIni[1]));
            // LocalDateTime final = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second)
            // LocalDateTime
        }
        }   
        

        
        
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
