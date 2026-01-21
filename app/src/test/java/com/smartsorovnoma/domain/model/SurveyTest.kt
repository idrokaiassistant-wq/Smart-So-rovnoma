package com.smartsorovnoma.domain.model

import org.junit.Test
import org.junit.Assert.*

/**
 * Survey domain model uchun testlar
 */
class SurveyTest {
    
    @Test
    fun `Survey can be created with required fields`() {
        val survey = Survey(
            id = "survey_1",
            title = "Test Survey",
            description = "Test Description",
            isActive = true
        )
        
        assertEquals("survey_1", survey.id)
        assertEquals("Test Survey", survey.title)
        assertEquals("Test Description", survey.description)
        assertTrue(survey.isActive)
        assertTrue(survey.questions.isEmpty())
    }
    
    @Test
    fun `Survey can have questions`() {
        val question = Question(
            id = "q1",
            surveyId = "survey_1",
            order = 0,
            text = "Test question?",
            type = QuestionType.TEXT,
            required = true
        )
        
        val survey = Survey(
            id = "survey_1",
            title = "Test Survey",
            description = "Test Description",
            isActive = true,
            questions = listOf(question)
        )
        
        assertEquals(1, survey.questions.size)
        assertEquals("q1", survey.questions[0].id)
    }
    
    @Test
    fun `Question has correct properties`() {
        val question = Question(
            id = "q1",
            surveyId = "survey_1",
            order = 0,
            text = "What is your name?",
            type = QuestionType.TEXT,
            required = true
        )
        
        assertEquals("q1", question.id)
        assertEquals("survey_1", question.surveyId)
        assertEquals(0, question.order)
        assertEquals("What is your name?", question.text)
        assertEquals(QuestionType.TEXT, question.type)
        assertTrue(question.required)
        assertNull(question.choices)
    }
    
    @Test
    fun `Question can have choices for SINGLE_CHOICE`() {
        val choices = listOf(
            Choice(id = "c1", questionId = "q1", text = "Option 1", value = 0),
            Choice(id = "c2", questionId = "q1", text = "Option 2", value = 1)
        )
        
        val question = Question(
            id = "q1",
            surveyId = "survey_1",
            order = 0,
            text = "Choose one:",
            type = QuestionType.SINGLE_CHOICE,
            required = true,
            choices = choices
        )
        
        assertEquals(2, question.choices?.size)
        assertEquals("Option 1", question.choices?.get(0)?.text)
    }
    
    @Test
    fun `QuestionType enum has all required types`() {
        val types = QuestionType.values()
        
        assertTrue(types.contains(QuestionType.TEXT))
        assertTrue(types.contains(QuestionType.NUMBER))
        assertTrue(types.contains(QuestionType.SINGLE_CHOICE))
        assertTrue(types.contains(QuestionType.MULTI_CHOICE))
        assertTrue(types.contains(QuestionType.RATING))
    }
    
    @Test
    fun `Choice has correct properties`() {
        val choice = Choice(
            id = "c1",
            questionId = "q1",
            text = "Test Option",
            value = 0
        )
        
        assertEquals("c1", choice.id)
        assertEquals("q1", choice.questionId)
        assertEquals("Test Option", choice.text)
        assertEquals(0, choice.value)
    }
}
