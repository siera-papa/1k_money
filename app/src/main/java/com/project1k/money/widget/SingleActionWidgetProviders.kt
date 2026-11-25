package com.project1k.money.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.project1k.money.R
import com.project1k.money.data.TransactionType
import com.project1k.money.ui.CalcActivity
import com.project1k.money.ui.QuickEntryActivity

abstract class SingleActionWidgetProvider(
    private val transactionType: String,
    private val glyph: String
) : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_single_action)
            views.setTextViewText(R.id.widget_action, glyph)
            views.setOnClickPendingIntent(
                R.id.widget_action,
                pending(context, transactionType, id)
            )
            manager.updateAppWidget(id, views)
        }
    }

    private fun pending(context: Context, type: String, appWidgetId: Int): PendingIntent {
        val typeOffset = when (type) {
            TransactionType.EXPENSE -> 1
            TransactionType.TRANSFER -> 2
            else -> 3
        }
        val intent = Intent(context, QuickEntryActivity::class.java)
            .putExtra(QuickEntryActivity.EXTRA_TYPE, type)
            .putExtra(QuickEntryActivity.EXTRA_FROM_WIDGET, true)
            .addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or
                    Intent.FLAG_ACTIVITY_NO_HISTORY
            )
        return PendingIntent.getActivity(
            context,
            appWidgetId * 10 + typeOffset,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}

class ExpenseWidgetProvider : SingleActionWidgetProvider(TransactionType.EXPENSE, "−")
class TransferWidgetProvider : SingleActionWidgetProvider(TransactionType.TRANSFER, "⇄")
class IncomeWidgetProvider : SingleActionWidgetProvider(TransactionType.INCOME, "+")

class CalcWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_single_action)
            views.setTextViewText(R.id.widget_action, "123")
            val intent = Intent(context, CalcActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or
                    Intent.FLAG_ACTIVITY_NO_HISTORY
            )
            views.setOnClickPendingIntent(
                R.id.widget_action,
                PendingIntent.getActivity(
                    context,
                    id * 10 + 4,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            manager.updateAppWidget(id, views)
        }
    }
}
