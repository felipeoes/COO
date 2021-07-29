import java.time.*;
import java.util.*;

public class GerenciadorDeSalas {
    private List<Sala> listaSalas;
    private HashMap<String, HashSet<Reserva>> relReservas;
    private HashSet<Reserva> listaReservas; // lista de reservas de um determinada sala

    class FalhaNaReservaException extends Exception {

        private Reserva reserva;

        public FalhaNaReservaException(Reserva reserva) {
            this.reserva = reserva;
        }

        public String getMessage() {
            return ("\t\t\tDados da reserva \n\n \tINICIO \t\tFIM \t\tSALA \n \t" + reserva.getInicio() + "\t"
                    + reserva.getFim() + "\t" + reserva.getSala());
        }
    }

    public void adicionaSalaChamada(String nome, int capacidadeMaxima, String descricao) {
        Sala sala = new Sala(nome, capacidadeMaxima, descricao);
        adicionaSala(sala);
    }

    public void removeSalaChamada(String nomeDaSala) {
        if (nomeDaSala.length() > 0) {
            Sala sala = buscaSala(nomeDaSala);
            if (sala != null)
                listaSalas.remove(sala);
        }
    }

    public List<Sala> listaDeSalas() {
        return this.listaSalas;
    }

    public void adicionaSala(Sala novaSala) {
        this.listaSalas.add(novaSala);
    }

    public Reserva reservaSalaChamada(String nomeDaSala, LocalDateTime dataInicial, LocalDateTime dataFinal)
            throws FalhaNaReservaException {
        if (nomeDaSala.length() > 0) {
            Sala sala = buscaSala(nomeDaSala);
            if (sala != null) {
                Reserva res = new Reserva(sala, dataInicial, dataFinal);
                listaReservas.add(res);
            }
        }

        return null;
    }

    public void cancelaReserva(Reserva cancelada) {

    }

    public Collection<Reserva> reservasParaSala(String nomeSala) {

        return relReservas.get(nomeSala);
    }

    public void imprimeReservasDaSala(String nomeSala) {

    }

    public Sala buscaSala(String nomeSala) {
        for (Sala sala : listaSalas) {
            if (sala.getNome().equals(nomeSala)) {
                return sala;
            }
        }

        return null;
    }
}
