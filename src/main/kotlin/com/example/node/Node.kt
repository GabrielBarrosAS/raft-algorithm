package com.example.node

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.concurrent.thread

enum class Tipo {
    SEGUIDOR,
    CANDIDATO,
    LIDER
}

@Component
class Node {

    private val log = LoggerFactory.getLogger(Node::class.java)

    var id: String = ""
    var idLider: String = ""
    var quantidadeDeNos: Int = 4;
    var tipo: Tipo = Tipo.SEGUIDOR;
    var termo: Int = 0;

    var votosRecebidos: Int = 0
    var votoEleicao: MutableMap<Int, Int> = mutableMapOf()

    var tempoInicial: Int = 0;
    var tempoEleicao: Int = 0;
    var timeout = false;


    init {
        thread {
            while (true){
                log.info("Aqui")
            }
        }.start()
    }

}