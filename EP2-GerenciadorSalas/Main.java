import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static boolean validaEmail(String email) { // método que usa regex para validar email
        final Pattern padraoEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = padraoEmail.matcher(email);
        return matcher.find();
    }

    public static boolean validaNumero(String num) { // método que usa regex para validar entrada de números
        // final Pattern padraoNum = Pattern.compile("[1-7]+");
        final Pattern padraoNum = Pattern.compile("\\b[1-7]+\\b");
        Matcher matcher = padraoNum.matcher(num);
        return matcher.matches();
    }

    public static LocalDateTime[] montaObjetoData(String[] dados) { // método auxiliar para criar dois objetos
                                                                    // LocalDateTime, um de inicio outro de fim, dada um
                                                                    // array de Strings em certo padrão
        String[] datas = dados[0].strip().split("/");
        int dia = Integer.parseInt(datas[0]);
        int mes = Integer.parseInt(datas[1]);
        int ano = Integer.parseInt(datas[2]);
        String[] horarios = dados[1].split("-");
        String[] hIni = horarios[0].split(":");
        String[] hFim = horarios[1].split(":");

        LocalDateTime inicio = LocalDateTime.of(ano, mes, dia, Integer.parseInt(hIni[0]), Integer.parseInt(hIni[1]));
        LocalDateTime fim = LocalDateTime.of(ano, mes, dia, Integer.parseInt(hFim[0]), Integer.parseInt(hFim[1]));

        LocalDateTime[] listaH = new LocalDateTime[2];
        listaH[0] = inicio;
        listaH[1] = fim;
        return listaH;
    }

    public static void marcadorDeReuniao() throws IOException {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y");
        List<String> participantes = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LocalDate dataInicial;
        LocalDate dataFinal;

        System.out.println(
                "Olá! Digite o período em que deseja marcar a reunião. Por exemplo, a entrada para um período entre 10 e 24 de abril de 2021 deve ser 10,24,8,2021");
        while (true) {
            try {
                String[] entrada = reader.readLine().split(",");
                int diaInicial = Integer.parseInt(entrada[0]);
                int diaFinal = Integer.parseInt(entrada[1]);
                int mes = Integer.parseInt(entrada[2]);
                int ano = Integer.parseInt(entrada[3]);

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
        while (!entrada.toLowerCase().equals("x")) {
            if (!validaEmail(entrada)) {
                System.out.println(
                        "Email informado não foi incluído na lista de participantes. Digite um email válido. Exemplo: fulano@gmail.com");
            } else if (participantes.contains(entrada)) {
                System.out.println(
                        "Email informado já está incluído na lista de participantes. Digite outro email ou \"X\" se já tiver incluído todos.");
            } else {
                participantes.add(entrada);
            }

            entrada = reader.readLine();
        }
        MarcadorDeReuniao marc = new MarcadorDeReuniao();

        marc.marcarReuniaoEntre(dataInicial, dataFinal, participantes);
        HashMap<LocalDateTime, LocalDateTime> relHorarios = new HashMap<>();
        HashMap<String, HashMap<LocalDateTime, LocalDateTime>> relDisponibilidade = new HashMap<>();
        LocalDateTime dataInicio = dataInicial.atTime(0, 0);
        LocalDateTime dataFim = dataFinal.atTime(0, 0);

        for (String participante : participantes) {
            System.out.println(participante
                    + " Por favor, indique seus horários inicial e final de disponibilidade para a reunião, respeitando o período estipulado. Sendo que o inicial deve ser separado do final por um hífen (-). Ex: 27/08/2021 14:25-18:00");
            System.out.println("Data inicial: " + formatador.format(dataInicial) + "\nData final: "
                    + formatador.format(dataFinal));

            String[] input = reader.readLine().split(" ");
            while (input[0].toLowerCase().equals("x")) { // caso em que o usuário já digitou X antes mesmo de indicar
                                                         // pelo menos um horário de disponibilidade.
                System.out.println("Indique ao menos um horário de disponibilidade");
                input = reader.readLine().split(" ");
            }

            while (!input[0].toLowerCase().equals("x")) {

                try {
                    String[] datas = input[0].strip().split("/");
                    int dia = Integer.parseInt(datas[0]);
                    int mes = Integer.parseInt(datas[1]);
                    int ano = Integer.parseInt(datas[2]);
                    String[] horarios = input[1].split("-");
                    String[] hIni = horarios[0].split(":");
                    String[] hFim = horarios[1].split(":");

                    LocalDateTime inicio = LocalDateTime.of(ano, mes, dia, Integer.parseInt(hIni[0]),
                            Integer.parseInt(hIni[1]));
                    LocalDateTime fim = LocalDateTime.of(ano, mes, dia, Integer.parseInt(hFim[0]),
                            Integer.parseInt(hFim[1]));

                    if ((inicio.isAfter(dataInicio)
                            || inicio.isEqual(dataInicio) && (fim.isBefore(dataFim) || fim.isEqual(dataFim)))) {
                        relHorarios.put(inicio, fim);
                        System.out.println(
                                "Caso deseje adicionar adicionar mais algum horário de disponibilidade, basta digitá-lo. Caso tenha finalizado, digite \"X\" e aperte Enter");
                    } else {
                        System.out.println(
                                "Horário não foi incluído na lista de disponibilidade. Digite um horário que respeite o período estipulado pelo responsável da reunião.\nData inicial: "
                                        + dataInicio.format(formatador) + "\nData final: "
                                        + dataFim.format(formatador));
                    }

                    input = reader.readLine().split(" ");
                } catch (Exception e) {
                    System.out.println("Por favor, digite um horário válido. Exemplo: 27/08/2021 14:25-18:00");
                    input = reader.readLine().split(" ");
                }
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

    public static void gerenciadorDeSalas() throws IOException, GerenciadorDeSalas.FalhaNaReservaException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Gostaria de utilizar o gerenciador de salas? Digite \"S\" para sim ou \"N\" para não");
        String input = reader.readLine().toLowerCase();

        while (!(input.equals("n") || input.equals("s"))) {
            System.out.println("Por favor, digite uma opção válida. \"S\" para sim ou \"N\" para não");
            input = reader.readLine();
        }
        if (input.equals("n"))
            return;

        GerenciadorDeSalas ger = new GerenciadorDeSalas();
        boolean encerrar = false;
        System.out.println(
                "Digite o número da opção que deseja: \n1 - Adicionar uma sala \n2 - Remover uma sala \n3 - Visualizar a lista de salas \n4 - Reservar uma sala\n5 - Cancelar uma reserva\n6 - Visualizar a lista de reservas de uma sala\n7 - Sair");
        input = reader.readLine();
        while (!validaNumero(input)) {
            System.out.println("Digite um número válido. Lembrando que as opções variam de 1 a 7.");
            input = reader.readLine();
        }

        while (!input.equals("7")) {
            switch (input) {
                case "1": {
                    System.out.println(
                            "Digite cada um dos seguintes campos seguidos de vírgula: nome da sala, local, observações, capacidade máxima. Exemplo: sala de jogos,EACH-USP,somente alunos da EACH-USP,45");
                    String[] dadosSala = reader.readLine().split(",");

                    while (true) {
                        try {
                            Sala sala = new Sala(dadosSala[0], dadosSala[1], dadosSala[2],
                                    Integer.parseInt(dadosSala[3]));
                            ger.adicionaSala(sala);
                            break;
                        } catch (Exception e) {
                            System.out.println(
                                    "Falha na criação da sala. Verifique se os dados digitados estão de acordo com o solicitado e tente novamente");
                            dadosSala = reader.readLine().split(",");
                        }
                    }
                }
                break;

                case "2": {
                    System.out.println("Digite o nome da sala a ser removida");
                    String nomeSala = reader.readLine();

                    ger.removeSalaChamada(nomeSala);
                }
                break;

                case "3": {
                    List<Sala> listaSalas = ger.listaDeSalas();
                    if(listaSalas.isEmpty()) System.out.println("Não há nenhuma sala neste gerenciador de salas");
                    
                    for (Sala sala : ger.listaDeSalas()) {
                        System.out.println(sala.toString());
                    }
                }
                break;

                case "4": {
                    System.out.println(
                            "Digite o nome da sala seguido do horário inicial e final da reserva, sendo que a sala é separada dos horários por uma vírgula e o horário inicial deve ser separado do final por um hífen (-). Exemplo: sala de jogos,27/08/2021 14:25-18:00");
                    String[] inputH = reader.readLine().split(",");
                    String[] datas = inputH[1].split(" ");
                    LocalDateTime[] horarios = montaObjetoData(datas);

                    ger.reservaSalaChamada(inputH[0], horarios[0], horarios[1]);
                }
                break;

                case "5": {
                    System.out.println(
                            "Digite o nome da sala seguido do horário inicial e final da reserva, sendo que a sala é separada dos horários por uma vírgula e o horário inicial deve ser separado do final por um hífen (-). Exemplo: sala de jogos,27/08/2021 14:25-18:00");
                    String[] inputH = reader.readLine().split(",");
                    String[] datas = inputH[1].split(" ");
                    LocalDateTime[] horarios = montaObjetoData(datas);

                    Reserva res = ger.buscaReserva(inputH[0], horarios[0], horarios[1]);
                    ger.cancelaReserva(res);
                }
                break;

                case "6": {
                    System.out.println("Digite o nome da sala que você deseja visualizar as reservas");
                    String nomeSala = reader.readLine();
                    ger.imprimeReservasDaSala(nomeSala);
                }
                break;

                case "7": {
                    encerrar = true;
                    System.out.println("Obrigado por utilizar nossos serviços. Até mais!"); 
                }
                break;
            }

            if (encerrar)
                break;
            System.out.println("\nDeseja continuar utilizando o gerenciador? Digite \"S\" para sim ou \"N\" para não");
            input = reader.readLine();
            while (!(input.equals("n") || input.equals("s"))) {
                System.out.println("Por favor, digite uma opção válida. \"S\" para sim ou \"N\" para não");
                input = reader.readLine();
            }
            if (input.equals("n"))
                return;
            else {
                System.out.println(
                        "Digite o número da opção que deseja: \n1 - Adicionar uma sala \n2 - Remover uma sala \n3 - Visualizar a lista de salas \n4 - Reservar uma sala\n5 - Cancelar uma reserva\n6 - Visualizar a lista de reservas de uma sala\n7 - Sair");
                input = reader.readLine();

                while (!validaNumero(input)) {
                    System.out.println("Digite um número válido. Lembrando que as opções variam de 1 a 7.");
                    input = reader.readLine();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, GerenciadorDeSalas.FalhaNaReservaException {
        // PARTE 1: disponibilidade de horários
        marcadorDeReuniao();

        // PARTE 2: Reserva de Salas
        gerenciadorDeSalas();
    }
}
