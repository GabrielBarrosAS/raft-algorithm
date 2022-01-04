package com.example.messaging

import com.example.dto.Candidato
import com.example.dto.NovoLiderEleito
import com.example.dto.NovoVoto
import com.example.dto.SinalAtividade
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class Producer (
    private val kafkaTemplate: KafkaTemplate<String, String>
){

    fun iniciarEleicao(nomeCandidato: String,termo: Int){
        val payload = jacksonObjectMapper().writeValueAsString(Candidato(termo,nomeCandidato))
        kafkaTemplate.send("nova-eleicao", payload)
    }

    fun votar(novoVoto: NovoVoto){
        val payload = jacksonObjectMapper().writeValueAsString(novoVoto)
        kafkaTemplate.send("novo-voto", payload)
    }

    fun infoNovoLider(novoLiderEleito: NovoLiderEleito){
        val payload = jacksonObjectMapper().writeValueAsString(novoLiderEleito)
        kafkaTemplate.send("info-novo-lider",payload)
    }

    fun sinalAtividade(nomeInstancia: String,termo: Int){
        val payload = jacksonObjectMapper().writeValueAsString(SinalAtividade(termo,nomeInstancia))
        kafkaTemplate.send("sinal-atividade",payload)
    }
}