import java.time.LocalDateTime;
import java.util.*;


abstract class Pessoa {
    private Instituicao instituicao;
    
    public Pessoa(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }
}

class Estudante extends Pessoa {
    private String nome;

    public Estudante(String nome, Instituicao instituicao) {
        super(instituicao);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

class Profissional extends Pessoa {
    private String nome;

    public Profissional(String nome, Instituicao instituicao) {
        super(instituicao);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

class Jurado extends Profissional {
    public Jurado(String nome, Instituicao instituicao) {
        super(nome, instituicao);
    }
}

abstract class Instituicao {
    private String nome;

    public Instituicao(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

class Universidade extends Instituicao {
    public Universidade(String nome) {
        super(nome);
    }
}

class Empresa extends Instituicao {
    public Empresa(String nome) {
        super(nome);
    }
}

class Equipe {
    private List<Estudante> membros = new ArrayList<>();

    public void adicionarMembro(Estudante estudante) {
        membros.add(estudante);
    }

    public List<Estudante> getMembros() {
        return membros;
    }
}

class Equipes {
    private static Equipes instancia;
    private List<Equipe> equipes = new ArrayList<>();

    private Equipes() {}

    public static Equipes getInstancia() {
        if (instancia == null) instancia = new Equipes();
        return instancia;
    }

    public void adicionarEquipe(Equipe equipe) {
        equipes.add(equipe);
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }
}

class Projeto {
    private Profissional orientador;
    private Equipe equipe;
    private int notaFinal;

    public Projeto(Profissional orientador, Equipe equipe) {
        this.orientador = orientador;
        this.equipe = equipe;
    }

    public void setNotaFinal(int notaFinal) {
        this.notaFinal = notaFinal;
    }

    public int getNotaFinal() {
        return notaFinal;
    }

    public Equipe getEquipe() {
        return equipe;
    }
}

interface Avaliavel {
    void calcularNotaFinal();
}

class Banca implements Avaliavel {
    private Projeto projetoAvaliado;
    private Map<Jurado, Integer> jurados = new HashMap<>();

    public Banca(Projeto projetoAvaliado) {
        this.projetoAvaliado = projetoAvaliado;
    }

    public void adicionarNota(Jurado jurado, int nota) {
        jurados.put(jurado, nota);
    }

    @Override
    public void calcularNotaFinal() {
        int soma = jurados.values().stream().mapToInt(Integer::intValue).sum();
        int media = soma / jurados.size();
        projetoAvaliado.setNotaFinal(media);
    }
}

class Sala {
    private String nome;

    public Sala(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

class Apresentacao {
    private Projeto projeto;
    private Avaliavel banca;
    private Sala local;
    private LocalDateTime dataHora;

    public Apresentacao(Projeto projeto, Avaliavel banca, Sala local, LocalDateTime dataHora) {
        this.projeto = projeto;
        this.banca = banca;
        this.local = local;
        this.dataHora = dataHora;
    }

    public void avaliar() {
        banca.calcularNotaFinal();
    }

    public Projeto getProjeto() {
        return projeto;
    }
}

class Apresentacoes {
    private static Apresentacoes instancia;
    private List<Apresentacao> apresentacoes = new ArrayList<>();

    private Apresentacoes() {}

    public static Apresentacoes getInstancia() {
        if (instancia == null) instancia = new Apresentacoes();
        return instancia;
    }

    public void adicionarApresentacao(Apresentacao apresentacao) {
        apresentacoes.add(apresentacao);
    }

    public List<Apresentacao> getApresentacoes() {
        return apresentacoes;
    }
}

public class atividade {
    public static void main(String[] args) {
        Universidade puc = new Universidade("PUC Minas");
        Empresa empresa = new Empresa("Empresa X");

        // Cria 2 equipes com 5 estudantes cada
        for (int i = 1; i <= 2; i++) {
            Equipe equipe = new Equipe();
            for (int j = 1; j <= 5; j++) {
                Estudante estudante = new Estudante("Aluno" + i + j, puc);
                equipe.adicionarMembro(estudante);
            }
            Equipes.getInstancia().adicionarEquipe(equipe);
        }

        for (Equipe equipe : Equipes.getInstancia().getEquipes()) {
            Profissional orientador = new Profissional("Orientador", empresa);
            Projeto projeto = new Projeto(orientador, equipe);

            Banca banca = new Banca(projeto);
            for (int k = 1; k <= 4; k++) {
                Jurado jurado = new Jurado("Jurado" + k, empresa);
                banca.adicionarNota(jurado, new Random().nextInt(4) + 6);
            }

            Sala sala = new Sala("Sala 10");
            Apresentacao apresentacao = new Apresentacao(projeto, banca, sala, LocalDateTime.now());
            apresentacao.avaliar();
            Apresentacoes.getInstancia().adicionarApresentacao(apresentacao);
        }

        System.out.println("Projetos aprovados:");
        Apresentacoes.getInstancia().getApresentacoes().stream()
            .filter(a -> a.getProjeto().getNotaFinal() >= 7)
            .forEach(a -> System.out.println("Projeto com nota: " + a.getProjeto().getNotaFinal()));
    }
}
