import java.io.*;
import java.util.*;
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
        final Pattern padraoNum = Pattern.compile("\\b[1-7]\\b");
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
        final DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y");
        List<String> participantes = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LocalDate dataInicial = LocalDate.of(1, 1, 1);
        LocalDate dataFinal = LocalDate.of(1, 1, 1);

        System.out.println(
                "Ola! Digite o periodo em que deseja marcar a reuniao. Por exemplo, a entrada para um periodo entre 10 e 24 de abril de 2021 deve ser 10,24,8,2021");
        String[] periodo = reader.readLine().split(",");
        if (periodo[0].toLowerCase().equals("x"))
            return;

        while (!periodo[0].toLowerCase().equals("x")) {
            try {
                int diaInicial = Integer.parseInt(periodo[0]);
                int diaFinal = Integer.parseInt(periodo[1]);
                int mes = Integer.parseInt(periodo[2]);
                int ano = Integer.parseInt(periodo[3]);

                dataInicial = LocalDate.of(ano, mes, diaInicial); // periodo
                dataFinal = LocalDate.of(ano, mes, diaFinal);

                break;
            } catch (Exception e) {
                System.out.println(
                        "Por favor, digite uma data válida. Exemplo: 5,10,11,2021 \nSendo que 5 representa o dia inicial do periodo, 10 representa o dia final, 11 eh o mês novembro e 2021 eh o ano \nPara sair, digite \"x\"");
                periodo = reader.readLine().split(",");
            }

            if (periodo[0].toLowerCase().equals("x"))
                return;
        }

        System.out.println(
                "Agora digite os emails de todos os participantes da reuniao, um por vez seguido de um \"Enter\" \nQuando finalizar, digite \"X\" e aperte \"Enter\"");

        String entrada = reader.readLine();
        while (!entrada.toLowerCase().equals("x")) {
            if (!validaEmail(entrada)) {
                System.out.println(
                        "Email informado nao foi incluido na lista de participantes. Digite um email valido. Exemplo: fulano@gmail.com");
            } else if (participantes.contains(entrada)) {
                System.out.println(
                        "Email informado ja esta incluido na lista de participantes. Digite outro email ou \"X\" se ja tiver incluido todos.");
            } else {
                participantes.add(entrada);
            }

            entrada = reader.readLine();
        }
        MarcadorDeReuniao marc = new MarcadorDeReuniao();

        marc.marcarReuniaoEntre(dataInicial, dataFinal, participantes);
        HashMap<LocalDateTime, LocalDateTime> relHorarios = new HashMap<>();
        HashMap<String, HashMap<LocalDateTime, LocalDateTime>> relDisponibilidade = new HashMap<>();

        for (String participante : participantes) {
            System.out.println(participante
                    + " Por favor, indique seus horários inicial e final de disponibilidade para a reuniao, respeitando o periodo estipulado. Sendo que o inicial deve ser separado do final por um hifen (-). Ex: 27/08/2021 14:25-18:00");
            System.out.println("Data inicial: " + formatador.format(dataInicial) + "\nData final: "
                    + formatador.format(dataFinal));

            String[] input = reader.readLine().split(" ");
            while (input[0].toLowerCase().equals("x")) { // caso em que o usuário já digitou X antes mesmo de indicar
                                                         // pelo menos um horário de disponibilidade.
                System.out.println("Indique ao menos um horario de disponibilidade");
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

                    marc.indicaDisponibilidadeDe(participante, inicio, fim);
                    System.out.println(
                            "Caso deseje adicionar adicionar mais algum horário de disponibilidade, basta digitá-lo. Caso tenha finalizado, digite \"X\" e aperte Enter");

                    input = reader.readLine().split(" ");
                } catch (Exception e) {
                    System.out.println("Por favor, digite um horário válido. Exemplo: 27/08/2021 14:25-18:00");
                    input = reader.readLine().split(" ");
                }
            }

            relDisponibilidade.put(participante, relHorarios);
            relHorarios = new HashMap<>();
            marc.relDatas = new ArrayList<HashMap<LocalDateTime, LocalDateTime>>();
        }
        marc.mostraSobreposicao();
    }

    public static void gerenciadorDeSalas() throws IOException, GerenciadorDeSalas.FalhaNaReservaException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Gostaria de utilizar o gerenciador de salas? Digite \"S\" para sim ou \"N\" para nao");
        String input = reader.readLine().toLowerCase();

        while (!(input.equals("n") || input.equals("s"))) {
            System.out.println("Por favor, digite uma opcao valida. \"S\" para sim ou \"N\" para nao");
            input = reader.readLine();
        }
        if (input.equals("n"))
            return;

        GerenciadorDeSalas ger = new GerenciadorDeSalas();
        boolean encerrar = false;
        System.out.println(
                "Digite o numero da opcao que deseja: \n1 - Adicionar uma sala \n2 - Remover uma sala \n3 - Visualizar a lista de salas \n4 - Reservar uma sala\n5 - Cancelar uma reserva\n6 - Visualizar a lista de reservas de uma sala\n7 - Sair");
        input = reader.readLine();
        while (!validaNumero(input)) {
            System.out.println("Digite um numero valido. Lembrando que as opcoes variam de 1 a 7.");
            input = reader.readLine();
        }

        while (!input.equals("7")) {
            switch (input) {
                case "1": {
                    System.out.println(
                            "Digite cada um dos seguintes campos seguidos de virgula: nome da sala, local, observacoes, capacidade maxima. Exemplo: sala de jogos,EACH-USP,somente alunos da EACH-USP,45");
                    String[] dadosSala = reader.readLine().split(",");

                    while (true) {
                        try {
                            Sala sala = new Sala(dadosSala[0], dadosSala[1], dadosSala[2],
                                    Integer.parseInt(dadosSala[3]));
                            ger.adicionaSala(sala);
                            break;
                        } catch (Exception e) {
                            System.out.println(
                                    "Falha na criacao da sala. Verifique se os dados digitados estao de acordo com o solicitado e tente novamente");
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
                    if (listaSalas.isEmpty())
                        System.out.println("Nao ha nenhuma sala neste gerenciador de salas");

                    for (Sala sala : ger.listaDeSalas()) {
                        System.out.println(sala.toString());
                    }
                }
                    break;

                case "4": {
                    System.out.println(
                            "Digite o nome da sala seguido do horario inicial e final da reserva, sendo que a sala eh separada dos horários por uma virgula e o horario inicial deve ser separado do final por um hifen (-). Exemplo: sala de jogos,27/08/2021 14:25-18:00");
                    String[] inputH = reader.readLine().split(",");
                    try {
                        String[] datas = inputH[1].split(" ");
                        LocalDateTime[] horarios = montaObjetoData(datas);

                        ger.reservaSalaChamada(inputH[0], horarios[0], horarios[1]);
                    } catch (Exception e) {
                        System.out.println("Nao foi possivel fazer a reserva.Por favor, verifique os dados digitados");
                    }

                }
                    break;

                case "5": {
                    System.out.println(
                            "Digite o nome da sala seguido do horario inicial e final da reserva, sendo que a sala eh separada dos horarios por uma virgula e o horario inicial deve ser separado do final por um hifen (-). Exemplo: sala de jogos,27/08/2021 14:25-18:00");
                    String[] inputH = reader.readLine().split(",");

                    try {
                        String[] datas = inputH[1].split(" ");
                        LocalDateTime[] horarios = montaObjetoData(datas);

                        Reserva res = ger.buscaReserva(inputH[0], horarios[0], horarios[1]);
                        if (res == null) {
                            System.out.println("Reserva inexistente");
                            break;
                        }

                        ger.cancelaReserva(res);
                    } catch (Exception e) {
                        System.out
                                .println("Nao foi possivel cancelar a reserva.Por favor verifique os dados digitados.");
                    }

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
                    System.out.println("Obrigado por utilizar nossos servicos. Ate mais!");
                }
                    break;
            }

            if (encerrar)
                break;
            System.out.println("\nDeseja continuar utilizando o gerenciador? Digite \"S\" para sim ou \"N\" para nao");
            input = reader.readLine();
            while (!(input.equals("n") || input.equals("s"))) {
                System.out.println("Por favor, digite uma opcao valida. \"S\" para sim ou \"N\" para nao");
                input = reader.readLine();
            }
            if (input.equals("n")) {
                System.out.println("Obrigado por utilizar nossos servicos. Ate mais!");
                return;
            }

            else {
                System.out.println(
                        "Digite o numero da opcao que deseja: \n1 - Adicionar uma sala \n2 - Remover uma sala \n3 - Visualizar a lista de salas \n4 - Reservar uma sala\n5 - Cancelar uma reserva\n6 - Visualizar a lista de reservas de uma sala\n7 - Sair");
                input = reader.readLine();

                while (!validaNumero(input)) {
                    System.out.println("Digite um numero valido. Lembrando que as opcoes variam de 1 a 7.");
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
