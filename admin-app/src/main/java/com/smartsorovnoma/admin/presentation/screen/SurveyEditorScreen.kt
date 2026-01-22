package com.smartsorovnoma.admin.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartsorovnoma.admin.data.model.Question
import com.smartsorovnoma.admin.data.model.QuestionType
import com.smartsorovnoma.admin.R
import com.smartsorovnoma.admin.presentation.viewmodel.EditorUiState
import com.smartsorovnoma.admin.presentation.viewmodel.SurveyEditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyEditorScreen(
    surveyId: String?,
    viewModel: SurveyEditorViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showQuestionDialog by remember { mutableStateOf(false) }
    var editingQuestionIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(surveyId) {
        viewModel.loadSurvey(surveyId)
    }

    LaunchedEffect(uiState) {
        if (uiState is EditorUiState.Saved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (surveyId == null) {
                            stringResource(R.string.title_create_survey)
                        } else {
                            stringResource(R.string.title_edit_survey)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.saveSurvey() }) {
                        Icon(Icons.Default.Save, contentDescription = stringResource(R.string.action_save))
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    editingQuestionIndex = null
                    showQuestionDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.action_add_question))
                Spacer(modifier = Modifier.size(8.dp))
                Text(stringResource(R.string.action_add_question))
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is EditorUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is EditorUiState.Error -> Text(
                    text = "${stringResource(R.string.error_prefix)}: ${state.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
                is EditorUiState.Content -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = stringResource(R.string.section_survey_details),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = state.survey.title,
                                        onValueChange = { viewModel.updateTitle(it) },
                                        label = { Text(stringResource(R.string.label_title)) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = state.survey.description,
                                        onValueChange = { viewModel.updateDescription(it) },
                                        label = { Text(stringResource(R.string.label_description)) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.label_questions),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                if (state.survey.questions.isNotEmpty()) {
                                    Text(
                                        text = stringResource(R.string.questions_count, state.survey.questions.size),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        if (state.survey.questions.isEmpty()) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = stringResource(R.string.empty_questions_title),
                                            style = MaterialTheme.typography.titleSmall,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = stringResource(R.string.empty_questions_message),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            itemsIndexed(state.survey.questions) { index, question ->
                                QuestionItem(
                                    question = question,
                                    onClick = {
                                        editingQuestionIndex = index
                                        showQuestionDialog = true
                                    },
                                    onDelete = { viewModel.removeQuestion(question) }
                                )
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }

    if (showQuestionDialog) {
        val state = uiState
        if (state is EditorUiState.Content) {
            val initialQuestion = editingQuestionIndex?.let { state.survey.questions.getOrNull(it) } ?: Question()
            QuestionEditorDialog(
                initialQuestion = initialQuestion,
                onDismiss = { showQuestionDialog = false },
                onSave = { question ->
                    if (editingQuestionIndex != null) {
                        viewModel.updateQuestion(editingQuestionIndex!!, question)
                    } else {
                        viewModel.addQuestion(question)
                    }
                    showQuestionDialog = false
                }
            )
        }
    }
}

@Composable
fun QuestionItem(question: Question, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(question.text, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = when (question.type) {
                        QuestionType.SINGLE_CHOICE -> stringResource(R.string.question_type_single_choice)
                        QuestionType.MULTIPLE_CHOICE -> stringResource(R.string.question_type_multiple_choice)
                        QuestionType.TEXT -> stringResource(R.string.question_type_text)
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row {
                IconButton(onClick = onClick) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.action_edit))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.action_delete))
                }
            }
        }
    }
}

@Composable
fun QuestionEditorDialog(
    initialQuestion: Question,
    onDismiss: () -> Unit,
    onSave: (Question) -> Unit
) {
    var text by remember { mutableStateOf(initialQuestion.text) }
    var type by remember { mutableStateOf(initialQuestion.type) }
    var optionsText by remember { mutableStateOf(initialQuestion.options.joinToString("\n")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.title_edit_question)) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(R.string.label_question_text)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = stringResource(R.string.label_question_type),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuestionType.values().forEach { t ->
                        val selected = type == t
                        Button(
                            onClick = { type = t },
                            colors = if (selected) {
                                androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            } else {
                                androidx.compose.material3.ButtonDefaults.buttonColors()
                            }
                        ) {
                            Text(
                                when (t) {
                                    QuestionType.SINGLE_CHOICE -> stringResource(R.string.question_type_single_choice)
                                    QuestionType.MULTIPLE_CHOICE -> stringResource(R.string.question_type_multiple_choice)
                                    QuestionType.TEXT -> stringResource(R.string.question_type_text)
                                }
                            )
                        }
                    }
                }

                if (type == QuestionType.SINGLE_CHOICE || type == QuestionType.MULTIPLE_CHOICE) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = optionsText,
                        onValueChange = { optionsText = it },
                        label = { Text(stringResource(R.string.label_options)) },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val options = if (type == QuestionType.TEXT) emptyList() else optionsText.split("\n").filter { it.isNotBlank() }
                onSave(initialQuestion.copy(text = text, type = type, options = options))
            }) {
                Text(stringResource(R.string.action_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) }
        }
    )
}
