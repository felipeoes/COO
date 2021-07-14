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

    }

    public void removeSalaChamada(String nomeDaSala) {

    }

    public List<Sala> listaDeSalas() {
        return this.listaSalas;
    }

    public void adicionaSala(Sala novaSala) {
        this.listaSalas.add(novaSala);
    }

    public Reserva reservaSalaChamada(String nomeDaSala, LocalDateTime dataInicial, LocalDateTime dataFinal)
            throws FalhaNaReservaException {

        return null;
    }

    public void cancelaReserva(Reserva cancelada) {

    }

    public Collection<Reserva> reservasParaSala(String nomeSala) {

        return relReservas.get(nomeSala);
    }

    public void imprimeReservasDaSala(String nomeSala) {

    }
}
