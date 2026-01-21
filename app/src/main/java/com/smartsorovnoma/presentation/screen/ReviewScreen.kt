package com.smartsorovnoma.presentation.screen

import androidx.compose.ui.res.stringResource
import com.smartsorovnoma.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartsorovnoma.domain.model.Question
import com.smartsorovnoma.presentation.common.AppCard
import com.smartsorovnoma.presentation.common.AppTopBar
import com.smartsorovnoma.presentation.common.SecondaryGradientButton
import com.smartsorovnoma.presentation.viewmodel.AnswerValue
import com.smartsorovnoma.presentation.viewmodel.QuestionFlowViewModel
import com.smartsorovnoma.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    surveyId: String,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit,
    viewModel: QuestionFlowViewModel = viewModel(
        factory = QuestionFlowViewModel.Factory(
            androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application,
            surveyId
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.review),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(Dimens.md)
        ) {
            // Survey title
            uiState.survey?.let { survey ->
                Text(
                    text = survey.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Dimens.md))
            }
            
            // Questions and answers
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.sm)
            ) {
                items(uiState.questions) { question ->
                    ReviewItem(
                        question = question,
                        answer = uiState.answers[question.id]
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Dimens.md))
            
            // Error message if submission failed
            if (uiState.submitError != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.xs),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.submitError!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(Dimens.sm)
                    )
                }
            }
            
            Box(modifier = Modifier.fillMaxWidth()) {
                if (uiState.isSubmitting) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                } else if (uiState.cooldownSeconds > 0) {
                     SecondaryGradientButton(
                        text = "Kuting: ${uiState.cooldownSeconds}s",
                        onClick = { },
                        enabled = false
                    )
                } else {
                    SecondaryGradientButton(
                        text = stringResource(R.string.submit),
                        onClick = {
                            coroutineScope.launch {
                                val success = viewModel.submitResponses()
                                if (success) {
                                    onSubmitClick()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewItem(
    question: Question,
    answer: AnswerValue?
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(Dimens.md)) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(Dimens.sm))
            
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            
            Spacer(modifier = Modifier.height(Dimens.sm))
            
            val formatted = formatAnswer(answer, question)
            Text(
                text = formatted.text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (formatted.isProvided)
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error
            )
        }
    }
}

private data class FormattedAnswer(
    val text: String,
    val isProvided: Boolean
)

@Composable
private fun formatAnswer(answer: AnswerValue?, question: Question): FormattedAnswer {
    val notProvided = stringResource(R.string.answer_not_provided)
    val notSelected = stringResource(R.string.answer_not_selected)
    val unknown = stringResource(R.string.answer_unknown)

    return when (answer) {
        is AnswerValue.Text -> {
            val value = answer.value.trim()
            if (value.isBlank()) FormattedAnswer(notProvided, false) else FormattedAnswer(value, true)
        }

        is AnswerValue.Number -> {
            val value = answer.value?.toString()
            if (value.isNullOrBlank()) FormattedAnswer(notProvided, false) else FormattedAnswer(value, true)
        }

        is AnswerValue.SingleChoice -> {
            val value = question.choices?.find { it.id == answer.choiceId }?.text
            if (value.isNullOrBlank()) FormattedAnswer(unknown, false) else FormattedAnswer(value, true)
        }

        is AnswerValue.MultiChoice -> {
            val value = question.choices
                ?.filter { it.id in answer.choiceIds }
                ?.joinToString(", ") { it.text }
                .orEmpty()
                .trim()
            if (value.isBlank()) FormattedAnswer(notSelected, false) else FormattedAnswer(value, true)
        }

        is AnswerValue.Rating -> FormattedAnswer("⭐".repeat(answer.value), true)

        is AnswerValue.Date -> {
            val formatted = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
                .format(java.util.Date(answer.value))
            FormattedAnswer(formatted, true)
        }

        null -> FormattedAnswer(notProvided, false)
    }
}
