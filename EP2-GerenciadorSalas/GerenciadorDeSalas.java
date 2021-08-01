import java.time.*;
import java.util.*;

public class GerenciadorDeSalas {
    private List<Sala> listaSalas; // lista com as salas existentes
    private HashSet<Reserva> listaReservas; // lista com as reservas existentes

    public GerenciadorDeSalas() {
        this.listaSalas = new ArrayList<>();
        this.listaReservas = new HashSet<>();
    }

    class FalhaNaReservaException extends Exception {

        public FalhaNaReservaException() {
        }

        public String getMessage() {
            return ("Falha na reserva! Confira os dados e tente novamente.");
        }
    }

    public void adicionaSalaChamada(String nome, int capacidadeMaxima, String descricao) {
        Sala sala = buscaSala(nome);
        if (sala != null) {
            System.out.println("A sala não foi adicionada pois já existe");
            return;
        }

        sala = new Sala(nome, capacidadeMaxima, descricao);
        adicionaSala(sala);
    }

    public void removeSalaChamada(String nomeDaSala) {
        if (nomeDaSala.length() > 0) {
            Sala sala = buscaSala(nomeDaSala);
            if (sala != null) {
                listaSalas.remove(sala);
                System.out.println("Sala removida com sucesso!");
            } else
                System.out.println("Sala inexistente");
        } else {
            System.out.println("Nome da sala inválido");
        }
    }

    public List<Sala> listaDeSalas() {
        return this.listaSalas;
    }

    public void adicionaSala(Sala novaSala) {
        Sala sala = buscaSala(novaSala.getNome());
        if (sala != null) {
            System.out.println("A sala não foi adicionada pois já existe");
            return;
        }

        this.listaSalas.add(novaSala);
        System.out.println("Sala adicionada com sucesso!");
    }

    public Reserva reservaSalaChamada(String nomeDaSala, LocalDateTime dataInicial, LocalDateTime dataFinal)
            throws FalhaNaReservaException {
        boolean inexistente, jahReservada;
        inexistente = jahReservada = false;
        Sala sala = buscaSala(nomeDaSala);
        String sucesso = "Reserva adicionada com sucesso!";

        FalhaNaReservaException f = new FalhaNaReservaException();

        try {
            if (nomeDaSala.length() > 0 && sala != null) {
                ArrayList<Reserva> reservasSala = sala.getReservas();
                if (!reservasSala.isEmpty()) {
                    for (Reserva reserva : reservasSala) {
                        if (dataInicial.isBefore(reserva.getFim()) || dataFinal.isAfter(reserva.getInicio())) {
                            jahReservada = true;
                            throw f;
                        }
                    }
                }
                Reserva res = new Reserva(sala, dataInicial, dataFinal);
                reservasSala.add(res);
                sala.setReservas(reservasSala);
                this.listaReservas.add(res);

                return res;
            } else {
                inexistente = true;
                throw f;
            }
        }

        finally {
            if (inexistente) {
                System.out.println("Sala inexistente!\n" + f.getMessage());
            } else if (jahReservada) {
                System.out.println("A sala informada já foi reservada!\n" + f.getMessage());
            } else {
                System.out.println(sucesso);
            }
        }
    }

    public void cancelaReserva(Reserva cancelada) {
        String sala = cancelada.getSala().getNome();
        Sala salaCancelada = buscaSala(sala);

        if (salaCancelada != null) {
            ArrayList<Reserva> reservasSala = salaCancelada.getReservas();
            if (reservasSala == null || reservasSala.isEmpty()) {
                System.out.println("Não há reservas para essa sala");
                return;
            }

            reservasSala.remove(cancelada);
            salaCancelada.setReservas(reservasSala);
            this.listaReservas.remove(cancelada);
            System.out.println("Reserva cancelada com sucesso!");
        } else {
            System.out.println("Sala inexistente");
        }
    }

    public Collection<Reserva> reservasParaSala(String nomeSala) {
        Sala sala = buscaSala(nomeSala);

        if (sala != null) {
            return sala.getReservas();
        }

        return null;
    }

    public void imprimeReservasDaSala(String nomeSala) {
        Collection<Reserva> reservas = reservasParaSala(nomeSala);
        if (reservas == null || reservas.isEmpty()) {
            System.out.println("Não há reservas para essa sala");
            return;
        }

        for (Reserva res : reservas) {
            System.out.println(res.toString());
        }
    }

    private Sala buscaSala(String nomeSala) {
        for (Sala sala : listaSalas) {
            if (sala.getNome().equals(nomeSala)) {
                return sala;
            }
        }

        return null;
    }

    public Reserva buscaReserva(String nomeSala, LocalDateTime inicio, LocalDateTime fim) {
        Sala sala = buscaSala(nomeSala);
        if (sala != null) {
            for (Reserva res : listaReservas) {
                if (res.getSala().equals(sala) && res.getInicio().equals(inicio) && res.getFim().equals(fim)) {
                    return res;
                }
            }
        }
        return null;
    }
}
