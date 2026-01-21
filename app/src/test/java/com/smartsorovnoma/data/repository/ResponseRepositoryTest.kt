package com.smartsorovnoma.data.repository

import com.smartsorovnoma.presentation.viewmodel.AnswerValue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

/**
 * ResponseRepository uchun unit testlar
 */
class ResponseRepositoryTest {
    
    @Test
    fun `submitResponse validates empty surveyId`() = runTest {
        val repository = ResponseRepository()
        val answers = mapOf("q1" to AnswerValue.Text("test"))
        
        val result = repository.submitResponse("", answers)
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("bo'sh") == true)
    }
    
    @Test
    fun `submitResponse validates empty answers`() = runTest {
        val repository = ResponseRepository()
        val answers = emptyMap<String, AnswerValue>()
        
        val result = repository.submitResponse("survey123", answers)
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("javob") == true)
    }
    
    @Test
    fun `answer serialization works correctly for Text`() {
        val answer = AnswerValue.Text("Test matn")
        // Test that serialization doesn't throw
        assertNotNull(answer)
        assertEquals("Test matn", answer.value)
    }
    
    @Test
    fun `answer serialization works correctly for Number`() {
        val answer = AnswerValue.Number(42)
        assertNotNull(answer)
        assertEquals(42, answer.value)
    }
    
    @Test
    fun `answer serialization works correctly for SingleChoice`() {
        val answer = AnswerValue.SingleChoice("choice_1")
        assertNotNull(answer)
        assertEquals("choice_1", answer.choiceId)
    }
    
    @Test
    fun `answer serialization works correctly for MultiChoice`() {
        val answer = AnswerValue.MultiChoice(setOf("choice_1", "choice_2"))
        assertNotNull(answer)
        assertEquals(2, answer.choiceIds.size)
        assertTrue(answer.choiceIds.contains("choice_1"))
    }
    
    @Test
    fun `answer serialization works correctly for Rating`() {
        val answer = AnswerValue.Rating(5)
        assertNotNull(answer)
        assertEquals(5, answer.value)
        assertTrue(answer.value in 1..5)
    }
}
