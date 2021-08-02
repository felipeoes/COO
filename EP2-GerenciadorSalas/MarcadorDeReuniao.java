import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MarcadorDeReuniao {
    private Collection<String> participantes;
    private LocalDate[] periodo;// periodo em que a reuniao pode ocorrer, determinado pelo responsavel da
                                // reuniao.
    private HashMap<String, List<HashMap<LocalDateTime, LocalDateTime>>> relDisponilidade; // relacao participante vs
                                                                                           // data de
    // disponilidade
    List<HashMap<LocalDateTime, LocalDateTime>> relDatas;
    private ArrayList<LocalDateTime> horariosIni; // set com os horários iniciais em que cada participante pode
                                                  // participar
                                                  // da
                                                  // reuniao
    private ArrayList<LocalDateTime> horariosFim; // set com os horários finais em que cada participante pode participar
                                                  // da
                                                  // reuniao

    private final DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y HH:mm");

    public MarcadorDeReuniao() {
        this.participantes = new HashSet<String>();
        this.relDisponilidade = new HashMap<>();
        this.horariosIni = new ArrayList<>();
        this.horariosFim = new ArrayList<>();
        this.relDatas = new ArrayList<HashMap<LocalDateTime, LocalDateTime>>();
    }

    public MarcadorDeReuniao(HashSet<String> participantes,
            HashMap<String, List<HashMap<LocalDateTime, LocalDateTime>>> relDisponilidade,
            List<HashMap<LocalDateTime, LocalDateTime>> relDatas, ArrayList<LocalDateTime> horariosIni,
            ArrayList<LocalDateTime> horariosFim) {
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
                    "\nHorario nao foi incluido na lista de disponibilidade pois nao respeita o periodo estipulado pelo responsavel da reuniao.\nData inicial:"
                            + periodoInicial.format(formatador).split(" ")[0] + "\nData final: "
                            + periodoFinal.format(formatador).split(" ")[0]);
        }
    }

    public Map<Object, List<LocalDateTime>> agrupaHorarios(Collection<LocalDateTime> listaHorarios) {
        TemporalField groupField = ChronoField.DAY_OF_YEAR;

        Map<Object, List<LocalDateTime>> result = listaHorarios.stream()
                .collect(Collectors.groupingBy(d -> d.get(groupField)));

        return result;
    }

    public boolean calculaSobreposicao() {
        Iterator<String> it = participantes.iterator();
        Map<Object, List<LocalDateTime>> iniAgrupado = agrupaHorarios(horariosIni);
        Map<Object, List<LocalDateTime>> fimAgrupado = agrupaHorarios(horariosFim);
        Iterator<Entry<Object, List<LocalDateTime>>> horariosFim = fimAgrupado.entrySet().iterator();
        boolean matchHorario;
        boolean existeHorario = false;

        for (Entry<Object, List<LocalDateTime>> horarios : iniAgrupado.entrySet()) {
            List<LocalDateTime> listaIni = horarios.getValue();
            if (listaIni.size() < participantes.size()) { // caso em que nem todos participantes escolheram aquele
                                                          // horário, então já não é match
                horariosFim.next();
                continue;
            }

            List<LocalDateTime> listaFim = horariosFim.next().getValue();
            LocalDateTime hIni = listaIni.stream().max(LocalDateTime::compareTo).get();
            LocalDateTime hFim = listaFim.stream().min(LocalDateTime::compareTo).get();

            matchHorario = true;

            it = participantes.iterator();
            while (it.hasNext() && matchHorario) {
                String participante = it.next();

                List<HashMap<LocalDateTime, LocalDateTime>> temp = relDisponilidade.get(participante);

                for (HashMap<LocalDateTime, LocalDateTime> horario : temp) {
                    LocalDateTime inicio = (LocalDateTime) horario.keySet().toArray()[0];
                    LocalDateTime fim = horario.get(inicio);
                    int diaIni = inicio.getDayOfMonth();

                    if (diaIni == hIni.getDayOfMonth()
                            && (hIni.isBefore(inicio) || fim.isBefore(hFim) || inicio.isEqual(hFim))) {
                        matchHorario = false;
                        break;
                    }
                }
            }

            if (matchHorario) {
                String fimFormatado = hFim.format(formatador).substring(11);
                System.out.println("\nA reuniao pode ser marcada no seguinte periodo: " + hIni.format(formatador) + "-"
                        + fimFormatado);

                existeHorario = true;
            }

        }
        return existeHorario;
    }

    public void mostraSobreposicao() {
        Iterator<String> it = participantes.iterator();
        int cont = 0;

        System.out.println("PARTICIPANTE  \t\t\tINICIO     \t\tFIM");
        while (it.hasNext()) {
            String participante = it.next();
            List<HashMap<LocalDateTime, LocalDateTime>> temp = relDisponilidade.get(participante);
            if (temp == null) {
                System.out.println("Nenhum horario de disponibilidade valido foi indicado");
                return;
            }

            for (HashMap<LocalDateTime, LocalDateTime> horario : temp) {
                LocalDateTime inicio = (LocalDateTime) horario.keySet().toArray()[0];
                LocalDateTime fim = horario.get(inicio);

                if (cont == 0) {
                    System.out.println(
                            participante + "   \t" + inicio.format(formatador) + "  \t\t" + fim.format(formatador));
                } else {
                    System.out.println(
                            participante + "  \t\t" + inicio.format(formatador) + "  \t\t" + fim.format(formatador));
                }
            }
            cont++;
        }

        if (!calculaSobreposicao())
            System.out.println("\nNão ha horarios que satisfacam todos os participantes");
    }
}