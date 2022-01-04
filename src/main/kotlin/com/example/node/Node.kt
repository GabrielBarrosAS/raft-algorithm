package com.example.node

import com.example.messaging.Producer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.concurrent.thread
import kotlin.random.Random

enum class Tipo { LIDER, CANDIDATO, SEGUIDOR }

@Component
@EnableScheduling
class Node(
    private val producer: Producer,
    @Value("\${nome.instancia}")
    val nomeInstancia: String = ""
) {

    fun novoTempoEspera(){
        tempoInicial = System.currentTimeMillis()
        tempoEspera = Random.nextLong(from = 10000, until = 20000)
        fimEspera = false
        log.info(nomeInstancia + " -> NOVO TEMPO -> " + tempoEspera.toString())
    }

    @Scheduled(fixedRate = 5000)
    fun sinalAtividade(){
        if(Tipo.LIDER == tipo){
            producer.sinalAtividade(nomeInstancia,termo)
        }
    }

    private val log = LoggerFactory.getLogger(Node::class.java)

    var idLider: String = ""
    var quantidadeDeNos: Int = 4
    var tipo: Tipo = Tipo.SEGUIDOR
    var termo: Int = 0

    var votosRecebidos: Int = 0
    var votoEleicao: MutableMap<Int, Boolean> = mutableMapOf()

    @Volatile
    var tempoInicial: Long = 0
    @Volatile
    var tempoEspera: Long = 0
    @Volatile
    var fimEspera = false

    init {
        thread {
            log.info(nomeInstancia + " esperando kafka por 90 segundos")
            Thread.sleep(90000)
            kotlin.run {
                novoTempoEspera()
                while (true) {
                    if (tipo != Tipo.LIDER) {
                        while (!fimEspera) {
                            if (System.currentTimeMillis() - tempoInicial >= tempoEspera) {
                                fimEspera = true
                            }
                        }

                        novoTempoEspera()

                        if(tipo != Tipo.LIDER) {
                            termo += 1
                            votosRecebidos = 1
                            tipo = Tipo.CANDIDATO
                            votoEleicao[termo] = true

                            log.info(nomeInstancia + " -> INICIANDO VOTAÇÃO")

                            producer.iniciarEleicao(nomeInstancia, termo)
                        }
                    }
                }
            }
        }
    }
}