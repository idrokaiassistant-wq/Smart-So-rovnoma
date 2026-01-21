package com.smartsorovnoma.domain.repository

import com.smartsorovnoma.domain.model.Survey
import com.smartsorovnoma.domain.model.SurveyDetail

interface SurveyRepository {
    fun getSurveys(): List<Survey>
    fun getSurveyById(id: String): Survey?
    fun getSurveyDetail(surveyId: String): SurveyDetail?
}
