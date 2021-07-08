import java.time.*;
import java.util.*;

public class GerenciadorDeSalas {

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

        return null;
    }

    public void adicionaSala(Sala novaSala) {

    }

    public Reserva reservaSalaChamada(String nomeDaSala, LocalDateTime dataInicial, LocalDateTime dataFinal) {

        return null;
    }

    public void cancelaReserva(Reserva cancelada) {

    }

    public Collection<Reserva> reservasParaSala(String nomeSala) {

        return null;
    }

    public void imprimeReservasDaSala(String nomeSala) {

    }
}
