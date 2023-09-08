enum class Nivel { BASICO, INTERMEDIARIO, AVANCADO }

data class ConteudoEducacional(
    // Conteudo educacional tem níveis de dificuldade, duração padrão é determinada pelo nível de dificuldade
    var nome: String,
    val nivel: Nivel = Nivel.INTERMEDIARIO // Nível padrão é Intermediário
) {
    val duracao: Int

    init {
        duracao = when (nivel) {
            Nivel.BASICO -> 60
            Nivel.INTERMEDIARIO -> 120
            Nivel.AVANCADO -> 180
        }
    }
}

data class Formacao(val nome: String, var conteudos: List<ConteudoEducacional>) {
    // formacao contém vários conteudos educacionais, e contem uma lista de alunos inscritos
    private val inscritos = mutableListOf<Usuario>()

    fun matricular(usuario: Usuario) {
        if (!inscritos.contains(usuario)) {
            inscritos.add(usuario)
            usuario.matricular(this)
        }
    }

    fun listarInscritos(): List<Usuario> {
        return inscritos
    }
}

data class Usuario(val nome: String) {
    // Usuarios lembram quais formacoes estão matriculados, podem concluir formações e adicionar a duracao em sua duração concluida
    val formacoesMatriculadas = mutableListOf<Formacao>()
    val formacoesConcluidas = mutableListOf<Formacao>()
    
    var duracaoConcluida: Int = 0
		private set

    fun matricular(formacao: Formacao) {
        formacoesMatriculadas.add(formacao)
    }

    fun concluirFormacao(formacao: Formacao) {
        if (formacoesMatriculadas.contains(formacao) && !formacoesConcluidas.contains(formacao)) {
            formacoesConcluidas.add(formacao)
            formacoesMatriculadas.remove(formacao)
            
          	// Atualiza a duracaoConcluida com a duração dos conteúdos da formação concluída
            duracaoConcluida += formacao.conteudos.sumBy { it.duracao }
        }
    }
}

fun main() {
    val usuario1 = Usuario("Alice")
    val usuario2 = Usuario("Bob")

    val conteudo1 = ConteudoEducacional("Introdução ao Kotlin", 120)
    val conteudo2 = ConteudoEducacional("Programação Orientada a Objetos", 90)
    val conteudo3 = ConteudoEducacional("Kotlin Avançado", 150)

    val formacao = Formacao("Formação Kotlin", mutableListOf(conteudo1, conteudo2, conteudo3))

    formacao.matricular(usuario1)
    formacao.matricular(usuario2)

    println("Inscritos na formação ${formacao.nome}: ${formacao.listarInscritos().size}")

    usuario1.concluirFormacao(formacao)

    println("Formações matriculadas por ${usuario1.nome}: ${usuario1.formacoesMatriculadas.size}")
    println("Formações concluídas por ${usuario1.nome}: ${usuario1.formacoesConcluidas.size}")
}