import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reserva {
    private Sala sala;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public Reserva(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        this.sala = sala;
        this.inicio = inicio;
        this.fim = fim;
    }

    public Reserva() {
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    @Override
    public String toString() {
        final DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/y HH:mm");

        return "NOME DA SALA: " + this.sala.getNome() + " \tINICIO: " + this.getInicio().format(formatador) + "\tFIM: "
                + this.getFim().format(formatador);
    }

}
