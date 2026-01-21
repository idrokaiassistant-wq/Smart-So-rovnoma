package com.smartsorovnoma.presentation.screen

import androidx.compose.ui.res.stringResource
import com.smartsorovnoma.R
import androidx.compose.material.icons.filled.Share

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartsorovnoma.presentation.common.AppCard
import com.smartsorovnoma.presentation.common.AppTopBar
import com.smartsorovnoma.presentation.viewmodel.SurveyDetailViewModel
import com.smartsorovnoma.ui.theme.Dimens
import com.smartsorovnoma.ui.theme.Success
import com.smartsorovnoma.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyDetailScreen(
    surveyId: String,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
    viewModel: SurveyDetailViewModel = viewModel(factory = SurveyDetailViewModel.Factory(surveyId))
) {
    val survey by viewModel.survey.collectAsState()
    val questionCount = survey?.questions?.size ?: 0
    val estimatedMinutes = ((questionCount * 30) / 60).coerceAtLeast(1) // har savol ~30 soniya
    
    Scaffold(
        topBar = {
            val context = androidx.compose.ui.platform.LocalContext.current
            AppTopBar(
                title = stringResource(R.string.survey_details),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val shareIntent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(
                                    android.content.Intent.EXTRA_TEXT,
                                    context.getString(R.string.share_msg, survey?.title.orEmpty())
                                )
                                type = "text/plain"
                            }
                            context.startActivity(
                                android.content.Intent.createChooser(
                                    shareIntent,
                                    context.getString(R.string.share)
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share),
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
                .padding(Dimens.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (survey != null) {
                val surveyData = survey!! // Local copy for smart cast
                // Main info card
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(Dimens.lg)) {
                        Text(
                            text = surveyData.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(Dimens.sm))
                        
                        Text(
                            text = surveyData.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(Dimens.lg))
                        
                        // Info badges row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
                        ) {
                            InfoBadge(
                                icon = Icons.Default.List,
                                label = stringResource(R.string.questions_count, questionCount),
                                modifier = Modifier.weight(1f)
                            )
                            InfoBadge(
                                icon = Icons.Default.DateRange,
                                label = stringResource(R.string.estimated_minutes, estimatedMinutes),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(Dimens.md))
                        
                        // Status badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (surveyData.isActive) Success else MaterialTheme.colorScheme.error)
                            )
                            Spacer(modifier = Modifier.width(Dimens.xs))
                            Text(
                                text = if (surveyData.isActive) {
                                    stringResource(R.string.status_active)
                                } else {
                                    stringResource(R.string.status_inactive)
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = if (surveyData.isActive) Success else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Start button with gradient
                com.smartsorovnoma.presentation.common.GradientButton(
                    text = stringResource(R.string.start),
                    onClick = onStartClick
                )
                
                Spacer(modifier = Modifier.height(Dimens.md))
            } else {
                Text(
                    text = stringResource(R.string.survey_not_found),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun InfoBadge(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = Dimens.md, vertical = Dimens.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(Dimens.xs))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

