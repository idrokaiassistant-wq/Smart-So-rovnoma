package com.smartsorovnoma.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartsorovnoma.R
import com.smartsorovnoma.domain.model.Choice
import com.smartsorovnoma.domain.model.Question
import com.smartsorovnoma.domain.model.QuestionType
import com.smartsorovnoma.presentation.viewmodel.AnswerValue
import com.smartsorovnoma.presentation.viewmodel.QuestionFlowViewModel
import com.smartsorovnoma.ui.theme.Gold400
import com.smartsorovnoma.ui.theme.Turquoise100
import com.smartsorovnoma.ui.theme.Turquoise500
import com.smartsorovnoma.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionFlowScreen(
    surveyId: String,
    onBackClick: () -> Unit,
    onReviewClick: () -> Unit,
    viewModel: QuestionFlowViewModel = viewModel(
        factory = QuestionFlowViewModel.Factory(
            androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application,
            surveyId
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentQuestion = viewModel.getCurrentQuestion()
    
    // Progress animation
    val progress = if (uiState.questions.isNotEmpty()) {
        (uiState.currentIndex + 1).toFloat() / uiState.questions.size.toFloat()
    } else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = stringResource(
                            R.string.question_progress,
                            uiState.currentIndex + 1,
                            uiState.questions.size
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress bar
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Turquoise100,
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentQuestion != null) {
                    // Question card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = currentQuestion.text,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            if (currentQuestion.required) {
                                Text(
                                    text = stringResource(R.string.required_label),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            // Question type render
                            QuestionInput(
                                question = currentQuestion,
                                currentAnswer = uiState.answers[currentQuestion.id],
                                onAnswerChange = { answer ->
                                    viewModel.updateAnswer(currentQuestion.id, answer)
                                }
                            )
                            
                            // Error message
                            uiState.currentError?.let { error ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = error,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Navigation buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (uiState.currentIndex > 0) {
                            Box(modifier = Modifier.weight(1f)) {
                                com.smartsorovnoma.presentation.common.OutlinedGradientButton(
                                    text = stringResource(R.string.back),
                                    onClick = { viewModel.goBack() }
                                )
                            }
                        }
                        
                        Box(modifier = Modifier.weight(1f)) {
                            com.smartsorovnoma.presentation.common.GradientButton(
                                text = if (viewModel.isLastQuestion()) {
                                    stringResource(R.string.review)
                                } else {
                                    stringResource(R.string.next)
                                },
                                onClick = { 
                                    if (viewModel.goNext()) {
                                        onReviewClick()
                                    }
                                },
                                height = 52.dp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                } else if (uiState.isLoading) {
                    // Loading state
                    com.smartsorovnoma.presentation.common.LoadingStateView(
                        message = stringResource(R.string.loading_questions)
                    )
                } else if (uiState.currentError != null) {
                    // Error state with retry
                    com.smartsorovnoma.presentation.common.EmptyStateView(
                        title = stringResource(R.string.error_title),
                        message = uiState.currentError ?: stringResource(R.string.error_generic),
                        actionText = stringResource(R.string.retry),
                        onAction = { viewModel.reload() }
                    )
                } else {
                    // Empty state
                    com.smartsorovnoma.presentation.common.EmptyStateView(
                        title = stringResource(R.string.questions_not_found_title),
                        message = stringResource(R.string.questions_not_found_msg),
                        actionText = stringResource(R.string.back),
                        onAction = onBackClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestionInput(
    question: Question,
    currentAnswer: AnswerValue?,
    onAnswerChange: (AnswerValue) -> Unit
) {
    when (question.type) {
        QuestionType.TEXT -> {
            val textValue = (currentAnswer as? AnswerValue.Text)?.value ?: ""
            OutlinedTextField(
                value = textValue,
                onValueChange = { onAnswerChange(AnswerValue.Text(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.answer_label)) },
                singleLine = false,
                maxLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }
        
        QuestionType.NUMBER -> {
            val numberValue = (currentAnswer as? AnswerValue.Number)?.value?.toString() ?: ""
            OutlinedTextField(
                value = numberValue,
                onValueChange = { 
                    if (it.isBlank()) {
                        onAnswerChange(AnswerValue.Number(null))
                        return@OutlinedTextField
                    }

                    val num = it.toIntOrNull()
                    if (num != null) {
                        onAnswerChange(AnswerValue.Number(num))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.enter_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }
        
        QuestionType.SINGLE_CHOICE -> {
            val selectedId = (currentAnswer as? AnswerValue.SingleChoice)?.choiceId
            Column(modifier = Modifier.selectableGroup()) {
                question.choices?.forEach { choice ->
                    SingleChoiceItem(
                        choice = choice,
                        selected = choice.id == selectedId,
                        onClick = { onAnswerChange(AnswerValue.SingleChoice(choice.id)) }
                    )
                }
            }
        }
        
        QuestionType.MULTI_CHOICE -> {
            val selectedIds = (currentAnswer as? AnswerValue.MultiChoice)?.choiceIds ?: emptySet()
            Column {
                question.choices?.forEach { choice ->
                    MultiChoiceItem(
                        choice = choice,
                        checked = choice.id in selectedIds,
                        onCheckedChange = { isChecked ->
                            val newSet = if (isChecked) {
                                selectedIds + choice.id
                            } else {
                                selectedIds - choice.id
                            }
                            onAnswerChange(AnswerValue.MultiChoice(newSet))
                        }
                    )
                }
            }
        }
        
        QuestionType.RATING -> {
            val rating = (currentAnswer as? AnswerValue.Rating)?.value ?: 0
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..5).forEach { value ->
                    FilterChip(
                        selected = rating == value,
                        onClick = { onAnswerChange(AnswerValue.Rating(value)) },
                        label = { 
                            Text(
                                text = "$value",
                                fontWeight = if (rating == value) FontWeight.Bold else FontWeight.Normal
                            ) 
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Gold400,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SingleChoiceItem(
    choice: Choice,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) Turquoise100 else MaterialTheme.colorScheme.surface
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Turquoise500 else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = choice.text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) Turquoise500 else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun MultiChoiceItem(
    choice: Choice,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (checked) Turquoise100 else MaterialTheme.colorScheme.surface
            )
            .border(
                width = if (checked) 2.dp else 1.dp,
                color = if (checked) Turquoise500 else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = choice.text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (checked) Turquoise500 else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

