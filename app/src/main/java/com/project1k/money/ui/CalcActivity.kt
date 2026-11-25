package com.project1k.money.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.project1k.money.MoneyApplication

class CalcActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        val store = (application as MoneyApplication).cashCalcStore
        setContent {
            Project1KMoneyTheme {
                val outsideClicks = remember { MutableInteractionSource() }
                val insideClicks = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = outsideClicks,
                            indication = null,
                            onClick = { finishAndRemoveTask() }
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 620.dp)
                            .clickable(
                                interactionSource = insideClicks,
                                indication = null,
                                onClick = {}
                            ),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        tonalElevation = 8.dp,
                        shadowElevation = 16.dp
                    ) {
                        CalcScreen(store = store, compact = true)
                    }
                }
            }
        }
    }
}
