
public class Sala {
    private String nome;
    private String local;
    private String observacoes;
    private int capacidade;

    public Sala(String nome, String local, String observacoes, int capacidade) {
        this.nome = nome;
        this.local = local;
        this.observacoes = observacoes;
        this.capacidade = capacidade;
    }

    public Sala(String nome, int capacidadeMaxima, String descricao) {
        this.nome = nome;
        this.observacoes = descricao;
        this.capacidade = capacidadeMaxima;
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
}
