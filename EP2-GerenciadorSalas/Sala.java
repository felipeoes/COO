import java.util.ArrayList;

public class Sala {
    private String nome;
    private String local;
    private String observacoes;
    private int capacidade;
    private ArrayList<Reserva> reservas; // lista de reservas associadas à essa sala;

    public Sala(String nome, String local, String observacoes, int capacidade) {
        this.nome = nome;
        this.local = local;
        this.observacoes = observacoes;
        this.capacidade = capacidade;
        this.reservas = new ArrayList<Reserva>();
    }

    public Sala(String nome, String local, String observacoes, int capacidade, ArrayList<Reserva> reservas) {
        this.nome = nome;
        this.local = local;
        this.observacoes = observacoes;
        this.capacidade = capacidade;
        this.reservas = reservas;
    }

    public Sala(String nome, int capacidadeMaxima, String descricao) {
        this.nome = nome;
        this.local = "";
        this.observacoes = descricao;
        this.capacidade = capacidadeMaxima;
        this.reservas = new ArrayList<Reserva>();
    }

    public Sala() {
        this.reservas = new ArrayList<Reserva>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return "NOME DA SALA: " + this.nome + "\tLOCAL: " + this.local + "\tCAPACIDADE: " + this.capacidade
                + "\tOBSERVAÇÕES: " + this.observacoes + "\tQTDE DE RESERVAS: " + this.reservas.size();
    }
}
