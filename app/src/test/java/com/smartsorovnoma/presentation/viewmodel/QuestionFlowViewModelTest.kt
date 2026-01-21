package com.smartsorovnoma.presentation.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuestionFlowViewModelTest {
    
    private lateinit var viewModel: QuestionFlowViewModel
    
    @Before
    fun setup() {
        // surveyId = "1" uchun mock repo'da 5 ta savol bor
        viewModel = QuestionFlowViewModel("1")
    }
    
    @Test
    fun `initial state should have questions loaded`() {
        val state = viewModel.uiState.value
        
        assertFalse(state.isLoading)
        assertNotNull(state.survey)
        assertEquals(5, state.questions.size)
        assertEquals(0, state.currentIndex)
    }
    
    @Test
    fun `goNext without answer on required field should show error`() {
        // Birinchi savol TEXT tipida va required = true
        val result = viewModel.goNext()
        
        assertFalse(result)
        assertEquals("Bu savol majburiy", viewModel.uiState.value.currentError)
    }
    
    @Test
    fun `goNext with valid answer should move to next question`() {
        val firstQuestion = viewModel.getCurrentQuestion()!!
        
        // Javob berish
        viewModel.updateAnswer(firstQuestion.id, AnswerValue.Text("Test javob"))
        
        // Keyingiga o'tish
        val result = viewModel.goNext()
        
        assertFalse(result) // oxirgi savol emas
        assertEquals(1, viewModel.uiState.value.currentIndex)
        assertEquals(null, viewModel.uiState.value.currentError)
    }
    
    @Test
    fun `goBack should move to previous question`() {
        // Avval keyingiga o'tamiz (javob bilan)
        viewModel.updateAnswer("q1_1", AnswerValue.Text("Test"))
        viewModel.goNext()
        assertEquals(1, viewModel.uiState.value.currentIndex)
        
        // Orqaga qaytamiz
        viewModel.goBack()
        assertEquals(0, viewModel.uiState.value.currentIndex)
    }
    
    @Test
    fun `goBack on first question should not change index`() {
        assertEquals(0, viewModel.uiState.value.currentIndex)
        
        viewModel.goBack()
        
        assertEquals(0, viewModel.uiState.value.currentIndex)
    }
    
    @Test
    fun `updateAnswer should save answer correctly`() {
        val questionId = "q1_1"
        val answer = AnswerValue.Text("My answer")
        
        viewModel.updateAnswer(questionId, answer)
        
        val savedAnswer = viewModel.uiState.value.answers[questionId]
        assertTrue(savedAnswer is AnswerValue.Text)
        assertEquals("My answer", (savedAnswer as AnswerValue.Text).value)
    }
    
    @Test
    fun `isLastQuestion should return true on last question`() {
        // 5 ta savol bor, har birida javob berib o'tamiz
        viewModel.updateAnswer("q1_1", AnswerValue.Text("1"))
        viewModel.goNext()
        viewModel.updateAnswer("q1_2", AnswerValue.Rating(5))
        viewModel.goNext()
        viewModel.updateAnswer("q1_3", AnswerValue.SingleChoice("c1_1"))
        viewModel.goNext()
        viewModel.goNext() // q1_4 optional
        
        // Endi 5-savolda
        assertEquals(4, viewModel.uiState.value.currentIndex)
        assertTrue(viewModel.isLastQuestion())
    }
    
    @Test
    fun `empty text answer should fail validation`() {
        viewModel.updateAnswer("q1_1", AnswerValue.Text(""))
        val result = viewModel.goNext()
        
        assertFalse(result)
        assertNotNull(viewModel.uiState.value.currentError)
    }
    
    @Test
    fun `rating answer should be valid between 1 and 5`() {
        // Birinchi savolga javob berib, ikkinchi savolga o'tamiz
        viewModel.updateAnswer("q1_1", AnswerValue.Text("Test"))
        viewModel.goNext()
        
        // Ikkinchi savol RATING tipida
        viewModel.updateAnswer("q1_2", AnswerValue.Rating(3))
        val result = viewModel.goNext()
        
        assertFalse(result) // oxirgi emas
        assertEquals(2, viewModel.uiState.value.currentIndex)
    }
}
