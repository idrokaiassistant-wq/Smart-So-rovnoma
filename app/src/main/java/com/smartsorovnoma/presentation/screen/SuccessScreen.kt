package com.smartsorovnoma.presentation.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.smartsorovnoma.R
import com.smartsorovnoma.ui.theme.Gold400
import com.smartsorovnoma.ui.theme.Dimens
import com.smartsorovnoma.ui.theme.Turquoise400
import com.smartsorovnoma.ui.theme.Turquoise600
import com.smartsorovnoma.ui.theme.White
import kotlinx.coroutines.delay
import kotlin.random.Random

// Confetti particle data class
private data class ConfettiParticle(
    var x: Float,
    var y: Float,
    val color: Color,
    var velocityX: Float,
    var velocityY: Float,
    val size: Float,
    var alpha: Float = 1f
)

@Composable
fun SuccessScreen(
    onHomeClick: () -> Unit
) {
    // Animation for icon
    val scale = remember { Animatable(0f) }
    
    // Confetti particles
    val confettiColors = listOf(
        Turquoise400,
        Turquoise600,
        Gold400,
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFF9800)
    )
    
    val particles = remember {
        mutableStateListOf<ConfettiParticle>().apply {
            repeat(50) {
                add(
                    ConfettiParticle(
                        x = Random.nextFloat() * 1000f,
                        y = Random.nextFloat() * -500f - 100f,
                        color = confettiColors.random(),
                        velocityX = Random.nextFloat() * 6f - 3f,
                        velocityY = Random.nextFloat() * 4f + 2f,
                        size = Random.nextFloat() * 12f + 4f
                    )
                )
            }
        }
    }
    
    // Animate confetti
    LaunchedEffect(Unit) {
        while (true) {
            delay(16) // ~60fps
            particles.forEach { particle ->
                particle.x += particle.velocityX
                particle.y += particle.velocityY
                particle.velocityY += 0.1f // gravity
                
                // Fade out when falling
                if (particle.y > 800f) {
                    particle.alpha = (1f - (particle.y - 800f) / 400f).coerceAtLeast(0f)
                }
                
                // Reset particle when it goes off screen
                if (particle.y > 1200f || particle.alpha <= 0f) {
                    particle.y = Random.nextFloat() * -200f - 100f
                    particle.x = Random.nextFloat() * 1000f
                    particle.velocityX = Random.nextFloat() * 6f - 3f
                    particle.velocityY = Random.nextFloat() * 4f + 2f
                    particle.alpha = 1f
                }
            }
        }
    }
    
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }
    
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Confetti canvas
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                particles.forEach { particle ->
                    drawCircle(
                        color = particle.color.copy(alpha = particle.alpha),
                        radius = particle.size,
                        center = Offset(particle.x, particle.y)
                    )
                }
            }
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.xl),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated success icon with gradient
                Box(
                    modifier = Modifier
                        .scale(scale.value)
                        .size(120.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = CircleShape,
                            ambientColor = Turquoise400
                        )
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Turquoise400, Turquoise600)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.success),
                        modifier = Modifier.size(64.dp),
                        tint = White
                    )
                }
                
                Spacer(modifier = Modifier.height(Dimens.xl))
                
                Text(
                    text = stringResource(R.string.sent_successfully),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(Dimens.sm))
                
                Text(
                    text = stringResource(R.string.sent_msg),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                com.smartsorovnoma.presentation.common.GradientButton(
                    text = stringResource(R.string.to_home),
                    onClick = onHomeClick
                )

                Spacer(modifier = Modifier.height(Dimens.md))

                val context = androidx.compose.ui.platform.LocalContext.current
                com.smartsorovnoma.presentation.common.SecondaryGradientButton(
                    text = stringResource(R.string.share_app),
                    onClick = {
                        val shareIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.share_app_msg))
                            type = "text/plain"
                        }
                        context.startActivity(android.content.Intent.createChooser(shareIntent, context.getString(R.string.share)))
                    }
                )
            }
        }
    }
}
