package io.pelle.kitchensink.app

import io.kotest.matchers.shouldBe
import io.pelle.kitchensink.model.ModelTestBed
import io.pelle.kitchensink.model.ModelTestBedExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ModelTestBedExtension::class)
class TodoServiceTest {

    @Test
    fun testCountOpenTodos(modelTestBed: ModelTestBed) {

        val testBed1 = modelTestBed.createUserWithOpenTodoItem()
        val testBed2 = modelTestBed.createUserWithDoneTodoItem()

        val service =
            TodoService(modelTestBed.groupsRepository, modelTestBed.usersRepository, modelTestBed.todoItemsRepository)

        service.totalOpenTodos(testBed1.group.name) shouldBe 1
        service.totalOpenTodos(testBed2.group.name) shouldBe 0
    }

}
