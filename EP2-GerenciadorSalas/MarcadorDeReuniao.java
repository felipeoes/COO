import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MarcadorDeReuniao {
    private HashSet<String> participantes;
    private HashMap<String, HashMap<LocalDateTime, LocalDateTime>> relDisponilidade; // relacao participante vs data de
                                                                                     // disponilidade
    private HashSet<LocalDateTime> horariosIni; // set com os horários iniciais em que cada participante pode participar
                                                // da
                                                // reuniao
    private HashSet<LocalDateTime> horariosFim; // set com os horários finais em que cada participante pode participar
                                                // da
                                                // reuniao

    public MarcadorDeReuniao() {
        this.participantes = new HashSet<>();
        this.relDisponilidade = new HashMap<>();
        this.horariosIni = new HashSet<>();
        this.horariosFim = new HashSet<>();
    }

    public MarcadorDeReuniao(HashSet<String> participantes,
            HashMap<String, HashMap<LocalDateTime, LocalDateTime>> relDisponilidade, HashSet<LocalDateTime> horariosIni,
            HashSet<LocalDateTime> horariosFim) {
        this.participantes = participantes;
        this.relDisponilidade = relDisponilidade;
        this.horariosIni = horariosIni;
        this.horariosFim = horariosFim;
    }

    public void marcarReuniaoEntre(LocalDate dataInicial, LocalDate dataFinal,
            Collection<String> listaDeParticipantes) {
                // Reserva res = new Reserva(sala, inicio, fim);
                

    }

    public void indicaDisponibilidadeDe(String participante, LocalDateTime inicio, LocalDateTime fim) {
        HashMap<LocalDateTime, LocalDateTime> relDatas = new HashMap<>();

        relDatas.put(inicio, fim);
        relDisponilidade.put(participante, relDatas);
        participantes.add(participante);
        horariosIni.add(inicio);
        horariosFim.add(fim);
    }

    public void mostraSobreposicao() {
        Iterator<String> it = participantes.iterator();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y hh:mm:ss");
        Iterator<LocalDateTime> itIni = horariosIni.iterator();
        Iterator<LocalDateTime> itFim = horariosFim.iterator();
        boolean matchHorarioIni = true;
        boolean matchHorarioFim = true;

        System.out.println("PARTICIPANTE  \t\tINICIO      \t\tFIM");
        while (it.hasNext()) {
            String participante = it.next();
            HashMap<LocalDateTime, LocalDateTime> temp = relDisponilidade.get(participante);
            LocalDateTime inicio = (LocalDateTime) temp.keySet().toArray()[0];
            LocalDateTime fim = temp.get(inicio);

            System.out.println(participante + "  \t" + inicio.format(formatador) + "  \t" + fim.format(formatador));
        }

        String listaHorariosIni = "";
        String listaHorariosFim = "";

        it = participantes.iterator();
        while (itIni.hasNext()) {
            LocalDateTime atual = itIni.next();

            while (it.hasNext()) {
                String participante = it.next();

                HashMap<LocalDateTime, LocalDateTime> temp = relDisponilidade.get(participante);
                LocalDateTime inicio = (LocalDateTime) temp.keySet().toArray()[0];

                if (!inicio.equals(atual))
                    matchHorarioIni = false;
            }

            listaHorariosIni += atual.format(formatador) + "\n";
        }

        it = participantes.iterator();
        while (itFim.hasNext()) {
            LocalDateTime atual = itFim.next();

            while (it.hasNext()) {
                String participante = it.next();

                HashMap<LocalDateTime, LocalDateTime> temp = relDisponilidade.get(participante);
                LocalDateTime inicio = (LocalDateTime) temp.keySet().toArray()[0];
                LocalDateTime fim = temp.get(inicio);

                if (!fim.equals(atual))
                    matchHorarioFim = false;
            }

            listaHorariosFim += atual.format(formatador) + "\n";
        }

        System.out.println(matchHorarioIni ? "Horários iniciais disponíveis: " + listaHorariosIni
                : "Nenhum horário inicial satisfaz a disponibilidade de todos os participantes");
        System.out.println(matchHorarioFim ? "Horários finais disponíveis: " + listaHorariosFim
                : "Nenhum horário final satisfaz a disponibilidade de todos os participantes");
    }
}