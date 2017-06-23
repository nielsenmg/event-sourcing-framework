package br.com.zup.realwave.common.eventstore.config

import akka.actor.ActorSystem
import eventstore.j.EsConnection
import eventstore.j.EsConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@ComponentScan(basePackages = arrayOf("br.com.zup.realwave.common.eventstore"))
open class EventStoreConfig {

    @Autowired
    private val applicationContext: ApplicationContext? = null

    @Autowired
    private val springExtension: SpringExtension? = null

    @Bean
    open fun actorSystem(): akka.actor.ActorSystem {

        val system = ActorSystem
                .create("AkkaEventStore", akkaConfiguration())
        springExtension!!.initialize(applicationContext!!)
        return system
    }

    @Bean
    open fun akkaConfiguration(): com.typesafe.config.Config {
        return com.typesafe.config.ConfigFactory.load()
    }

    @Bean
    open fun createConnection(@Autowired actorSystem: ActorSystem): EsConnection = EsConnectionFactory.create(actorSystem)

}