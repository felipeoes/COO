import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MarcadorDeReuniao {
    private Collection<String> participantes;
    private LocalDate[] periodo;// periodo em que a reuniao pode ocorrer, determinado pelo responsavel da
                                // reuniao.
    private HashMap<String, List<HashMap<LocalDateTime, LocalDateTime>>> relDisponilidade; // relacao participante vs
                                                                                           // data de
    // disponilidade
    List<HashMap<LocalDateTime, LocalDateTime>> relDatas;
    private HashSet<LocalDateTime> horariosIni; // set com os horários iniciais em que cada participante pode participar
                                                // da
                                                // reuniao
    private HashSet<LocalDateTime> horariosFim; // set com os horários finais em que cada participante pode participar
                                                // da
                                                // reuniao

    public MarcadorDeReuniao() {
        this.participantes = new HashSet<String>();
        this.relDisponilidade = new HashMap<>();
        this.horariosIni = new HashSet<>();
        this.horariosFim = new HashSet<>();
        this.relDatas = new ArrayList<HashMap<LocalDateTime, LocalDateTime>>();
    }

    public MarcadorDeReuniao(HashSet<String> participantes,
            HashMap<String, List<HashMap<LocalDateTime, LocalDateTime>>> relDisponilidade,
            List<HashMap<LocalDateTime, LocalDateTime>> relDatas, HashSet<LocalDateTime> horariosIni,
            HashSet<LocalDateTime> horariosFim) {
        this.participantes = participantes;
        this.relDisponilidade = relDisponilidade;
        this.relDatas = relDatas;
        this.horariosIni = horariosIni;
        this.horariosFim = horariosFim;
    }

    public void marcarReuniaoEntre(LocalDate dataInicial, LocalDate dataFinal,
            Collection<String> listaDeParticipantes) {

        this.participantes = listaDeParticipantes;
        this.periodo = new LocalDate[] { dataInicial, dataFinal };
    }

    public void indicaDisponibilidadeDe(String participante, LocalDateTime inicio, LocalDateTime fim) {
        HashMap<LocalDateTime, LocalDateTime> datas = new HashMap<>();
        LocalDateTime periodoInicial = periodo[0].atTime(0, 0);
        LocalDateTime periodoFinal = periodo[1].atTime(0, 0);

        if ((inicio.isAfter(periodoInicial) || inicio.isEqual(periodoInicial))
                && (fim.isBefore(periodoFinal) || fim.isEqual(periodoFinal))) {
            datas.put(inicio, fim);
            relDatas.add(datas);
            relDisponilidade.put(participante, relDatas);
            horariosIni.add(inicio);
            horariosFim.add(fim);
        }

        else {
            System.out.println(
                    "Indique um horário de disponibilidade dentro do período estipulado pelo responsável da reunião");
        }
    }

    public void verificaSobreposicao() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y HH:mm:ss");
        Iterator<String> it = participantes.iterator();
        // Iterator<LocalDateTime> itIni = horariosIni.iterator();
        Iterator<LocalDateTime> itFim = horariosFim.iterator();

        for (Iterator<LocalDateTime> itIni = horariosIni.iterator(); it.hasNext();) {
            LocalDateTime horarioInicial = itIni.next();
            LocalDateTime horarioFinal = itFim.next();

            while (it.hasNext()) {
                String participante = it.next();

                List<HashMap<LocalDateTime, LocalDateTime>> temp = relDisponilidade.get(participante);

                for (HashMap<LocalDateTime, LocalDateTime> horario : temp) {
                    LocalDateTime inicio = (LocalDateTime) horario.keySet().toArray()[0];
                    LocalDateTime fim = horario.get(inicio);

                    if (!(horarioInicial.isAfter(inicio) || horarioInicial.isEqual(inicio))) {
                        horariosIni.remove(horarioInicial);
                    }

                    if (!(horarioFinal.isBefore(fim) || horarioFinal.isEqual(fim))) {
                        horariosFim.remove(horarioFinal);
                    }
                }
            }
        }

        System.out.println("Horários iniciais disponíveis: ");
        for (LocalDateTime ini : horariosIni) {
            System.out.println(ini.format(formatador));
        }

        System.out.println("Horários finais disponíveis: ");
        for (LocalDateTime fim : horariosFim) {
            System.out.println(fim.format(formatador));
        }

    }

    public void mostraSobreposicao() {
        Iterator<String> it = participantes.iterator();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y HH:mm:ss");
        int cont = 0;

        System.out.println("PARTICIPANTE  \t\t\tINICIO      \t\tFIM");
        while (it.hasNext()) {
            String participante = it.next();
            List<HashMap<LocalDateTime, LocalDateTime>> temp = relDisponilidade.get(participante);
            for (HashMap<LocalDateTime, LocalDateTime> horario : temp) {
                LocalDateTime inicio = (LocalDateTime) horario.keySet().toArray()[0];
                LocalDateTime fim = horario.get(inicio);

                if (cont == 0) {
                    System.out.println(
                            participante + "   " + inicio.format(formatador) + "  \t" + fim.format(formatador));
                } else {
                    System.out.println(
                            participante + "  \t" + inicio.format(formatador) + "  \t" + fim.format(formatador));
                }
                cont++;
            }

        }

        verificaSobreposicao();
    }
}