package com.smartsorovnoma.presentation.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartsorovnoma.domain.model.Survey
import com.smartsorovnoma.presentation.common.AppCard
import com.smartsorovnoma.presentation.common.AppSearchField
import com.smartsorovnoma.presentation.common.AppTopBar
import com.smartsorovnoma.presentation.common.EmptyStateView
import com.smartsorovnoma.presentation.common.ErrorStateView
import com.smartsorovnoma.presentation.common.ShimmerSurveyItem
import com.smartsorovnoma.presentation.viewmodel.SurveyListViewModel
import com.smartsorovnoma.ui.theme.Turquoise400
import com.smartsorovnoma.ui.theme.Turquoise600
import com.smartsorovnoma.ui.theme.White
import com.smartsorovnoma.ui.theme.Dimens

import androidx.compose.ui.res.stringResource
import com.smartsorovnoma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyListScreen(
    onSurveyClick: (String) -> Unit,
    viewModel: SurveyListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val contentState = when {
        uiState.isLoading && uiState.surveys.isEmpty() -> SurveyListContentState.Loading
        uiState.error != null && uiState.surveys.isEmpty() -> SurveyListContentState.Error
        !uiState.isLoading && uiState.surveys.isEmpty() -> SurveyListContentState.Empty
        uiState.filteredSurveys.isEmpty() && uiState.searchQuery.isNotEmpty() -> SurveyListContentState.NoResults
        else -> SurveyListContentState.Content
    }
    
    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(R.string.surveys))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Search bar
            AppSearchField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .padding(Dimens.md),
                onClear = { viewModel.onSearchQueryChange("") }
            )
            
            // Content
            Box(modifier = Modifier.fillMaxSize()) {
                Crossfade(targetState = contentState, label = "survey_list_content_crossfade") { state ->
                    when (state) {
                        SurveyListContentState.Loading -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(5) {
                                    ShimmerSurveyItem()
                                }
                            }
                        }

                        SurveyListContentState.Error -> {
                            ErrorStateView(
                                message = uiState.error ?: stringResource(R.string.error_generic),
                                onRetry = { viewModel.refresh() }
                            )
                        }

                        SurveyListContentState.Empty -> {
                            EmptyStateView(
                                title = stringResource(R.string.no_surveys),
                                message = stringResource(R.string.no_surveys_msg),
                                actionText = stringResource(R.string.refresh),
                                onAction = { viewModel.refresh() }
                            )
                        }

                        SurveyListContentState.NoResults -> {
                            EmptyStateView(
                                icon = Icons.Default.Search,
                                title = stringResource(R.string.no_results),
                                message = stringResource(R.string.no_results_msg, uiState.searchQuery)
                            )
                        }

                        SurveyListContentState.Content -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(
                                    items = uiState.filteredSurveys,
                                    key = { survey -> survey.id }
                                ) { survey ->
                                    SurveyItem(
                                        survey = survey,
                                        onClick = { onSurveyClick(survey.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class SurveyListContentState {
    Loading,
    Error,
    Empty,
    NoResults,
    Content
}

@Composable
private fun SurveyItem(
    survey: Survey,
    onClick: () -> Unit
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with gradient background
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Turquoise400, Turquoise600)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(Dimens.md))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = survey.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = survey.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Question count indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.questions_count, survey.questions.size),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // Arrow indicator
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
