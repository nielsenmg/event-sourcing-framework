package br.com.zup.eventsourcing

import br.com.zup.eventsourcing.config.BaseTest
import br.com.zup.eventsourcing.domain.MyAggregate
import br.com.zup.eventsourcing.domain.MyAggregateRepository
import eventstore.WrongExpectedVersionException
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

/**
 * Created by zacacj on 6/20/2017.
 */
class MyAggregateRepositoryTest : BaseTest() {

    @Autowired
    lateinit var myAggregateRepository: MyAggregateRepository

    @Test
    fun saveMyAggregateCreate() {
        val id = UUID.randomUUID().toString()
        val myAggregate = MyAggregate(AggregateId(id))
        val metaData = MetaData()
        metaData.set("teste", "teste")
        myAggregateRepository.save(myAggregate, metaData)
    }

    @Test
    fun saveMyAggregateCreateAndGet() {
        val id = UUID.randomUUID().toString()
        val myAggregate = MyAggregate(AggregateId(id))
        val metaData = MetaData()
        metaData.set("teste2", myAggregate)
        myAggregateRepository.save(myAggregate, metaData)
        val myAggregateGot = myAggregateRepository.get(myAggregate.id)
        assertEquals(myAggregate, myAggregateGot)
    }

    @Test
    fun createAndModifyAggregate() {
        val id = UUID.randomUUID().toString()

        var myAggregate = MyAggregate(AggregateId(id))
        myAggregate.modify()
        myAggregate.modify()
        val metaData = MetaData()
        metaData.set("teste2", myAggregate)
        myAggregateRepository.save(myAggregate, metaData)

        val loadedFromEventStore = myAggregateRepository.get(myAggregate.id)

        assertEquals(myAggregate, loadedFromEventStore)
        assertEquals("ModifyEvent", loadedFromEventStore.status)
        assertEquals(2, loadedFromEventStore.modificationHistory.size)
        assertEquals(2, loadedFromEventStore.version.value)
    }

    @Test(expected = WrongExpectedVersionException::class)
    fun saveWithWrongExpectedVersion() {
        val id = UUID.randomUUID().toString()
        val myAggregate = MyAggregate(AggregateId(id))
        val metaData = MetaData()
        metaData.set("teste2", myAggregate)
        myAggregate.version = AggregateVersion(3)
        myAggregateRepository.save(myAggregate, metaData)
    }

}