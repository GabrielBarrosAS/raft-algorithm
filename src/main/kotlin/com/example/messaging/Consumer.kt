package com.example.messaging

import com.example.dto.Candidato
import com.example.dto.NovoLiderEleito
import com.example.dto.NovoVoto
import com.example.dto.SinalAtividade
import com.example.node.Node
import com.example.node.Tipo
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class Consumer (
    private val node: Node,
    private val producer: Producer
){

    private val log = LoggerFactory.getLogger(Consumer::class.java)

    @KafkaListener(topics = ["nova-eleicao"])
    fun novaEleicao(@Payload payload: String){
        node.novoTempoEspera()
        val candidato:Candidato = jacksonObjectMapper().readValue(payload);
        if(node.nomeInstancia != candidato.nomeInstancia) {

            if(node.votoEleicao[candidato.termo] == null){

                node.votoEleicao[candidato.termo] = true;
                node.termo+=1

                log.info(node.nomeInstancia + " votando em -> " + candidato.nomeInstancia)

                producer.votar(NovoVoto(candidato.nomeInstancia, node.nomeInstancia));
            }else{
                log.info(node.nomeInstancia + " já votou na eleição " + node.termo)
            }
        }
    }

    @KafkaListener(topics = ["novo-voto"])
    fun novoVoto(@Payload payload: String){
        node.novoTempoEspera();
        val novoVoto:NovoVoto = jacksonObjectMapper().readValue(payload);
        if(novoVoto.nomeCandidato == node.nomeInstancia){

            log.info(node.nomeInstancia + " recebeu o voto -> " + novoVoto.nomeEleitor);

            node.votosRecebidos+=1

            if(node.votosRecebidos > (node.quantidadeDeNos / 2)){

                node.tipo = Tipo.LIDER;
                node.idLider = node.nomeInstancia
                log.info(node.nomeInstancia + " fui eleito novo lider")

                producer.infoNovoLider(NovoLiderEleito(node.termo,node.nomeInstancia));

            } else{
                node.novoTempoEspera()
            }
        }
    }

    @KafkaListener(topics = ["info-novo-lider"])
    fun infoNovoLider(@Payload payload: String){
        val novoLiderEleito:NovoLiderEleito = jacksonObjectMapper().readValue(payload);
        if (node.nomeInstancia != novoLiderEleito.nomeLider){
            node.novoTempoEspera()
            if(node.tipo != Tipo.LIDER || novoLiderEleito.termo > node.termo){
                log.info(node.nomeInstancia + " -> novo lider é " + novoLiderEleito.nomeLider)
                node.idLider = novoLiderEleito.nomeLider
                node.termo = novoLiderEleito.termo
                node.tipo = Tipo.SEGUIDOR
            }
        }
    }
    @KafkaListener(topics = ["sinal-atividade"])
    fun sinalAtividade(@Payload payload: String){
        node.novoTempoEspera()
        val sinalAtividade: SinalAtividade = jacksonObjectMapper().readValue(payload)
        if(sinalAtividade.nomeInstancia != node.nomeInstancia){
            if(node.termo < sinalAtividade.termo){
                log.info(node.nomeInstancia + " -> novo lider " + sinalAtividade.nomeInstancia)
                node.idLider = sinalAtividade.nomeInstancia
                node.termo = sinalAtividade.termo
                node.tipo = Tipo.SEGUIDOR;
            }else{
                log.info(sinalAtividade.nomeInstancia + " ainda está ativo!")
            }
        }
    }
}