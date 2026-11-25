package com.project1k.money

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.project1k.money.ui.MoneyApp
import com.project1k.money.ui.MoneyViewModel
import com.project1k.money.ui.Project1KMoneyTheme

class MainActivity : ComponentActivity() {
    private val vm by viewModels<MoneyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project1KMoneyTheme { MoneyApp(vm) }
        }
    }
}
