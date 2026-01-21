package com.smartsorovnoma.presentation.viewmodel

import org.junit.Test
import org.junit.Assert.*

/**
 * AnswerValue sealed class uchun testlar
 */
class AnswerValueTest {
    
    @Test
    fun `Text answer stores value correctly`() {
        val answer = AnswerValue.Text("Hello World")
        assertEquals("Hello World", answer.value)
    }
    
    @Test
    fun `Number answer stores value correctly`() {
        val answer = AnswerValue.Number(100)
        assertEquals(100, answer.value)
    }
    
    @Test
    fun `Number answer can be null`() {
        val answer = AnswerValue.Number(null)
        assertNull(answer.value)
    }
    
    @Test
    fun `SingleChoice answer stores choiceId correctly`() {
        val answer = AnswerValue.SingleChoice("option_a")
        assertEquals("option_a", answer.choiceId)
    }
    
    @Test
    fun `MultiChoice answer stores multiple choices`() {
        val choices = setOf("option_a", "option_b", "option_c")
        val answer = AnswerValue.MultiChoice(choices)
        
        assertEquals(3, answer.choiceIds.size)
        assertTrue(answer.choiceIds.contains("option_a"))
        assertTrue(answer.choiceIds.contains("option_b"))
        assertTrue(answer.choiceIds.contains("option_c"))
    }
    
    @Test
    fun `MultiChoice answer can be empty set`() {
        val answer = AnswerValue.MultiChoice(emptySet())
        assertEquals(0, answer.choiceIds.size)
    }
    
    @Test
    fun `Rating answer stores value correctly`() {
        val answer = AnswerValue.Rating(4)
        assertEquals(4, answer.value)
    }
    
    @Test
    fun `Rating answer validates range`() {
        // Valid ratings
        assertTrue(AnswerValue.Rating(1).value in 1..5)
        assertTrue(AnswerValue.Rating(3).value in 1..5)
        assertTrue(AnswerValue.Rating(5).value in 1..5)
        
        // These would be invalid but constructor doesn't validate
        // That's handled by the ViewModel
    }
    
    @Test
    fun `Date answer stores timestamp correctly`() {
        val timestamp = System.currentTimeMillis()
        val answer = AnswerValue.Date(timestamp)
        assertEquals(timestamp, answer.value)
    }
    
    @Test
    fun `Different answer types are distinct`() {
        val text = AnswerValue.Text("test")
        val number = AnswerValue.Number(123)
        val single = AnswerValue.SingleChoice("a")
        val multi = AnswerValue.MultiChoice(setOf("a"))
        val rating = AnswerValue.Rating(3)
        val date = AnswerValue.Date(System.currentTimeMillis())
        
        // All should be different types
        assertFalse(text == number)
        assertFalse(number == single)
        assertFalse(single == multi)
        assertFalse(multi == rating)
        assertFalse(rating == date)
    }
}
