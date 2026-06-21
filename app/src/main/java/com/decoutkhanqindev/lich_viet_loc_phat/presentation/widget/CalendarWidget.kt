package com.decoutkhanqindev.lich_viet_loc_phat.presentation.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.decoutkhanqindev.lich_viet_loc_phat.data.repository.CalendarRepositoryImpl
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.lunar_math_algorithm.LunarMathAlgorithmDataSourceImpl
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset.StaticAssetDataSourceImpl
import com.decoutkhanqindev.lich_viet_loc_phat.device.SharedPrefsManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDailyMetadataUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDaysInMonthUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.model.toUiModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.context.GlobalContext

class CalendarWidget : GlanceAppWidget() {

    private val repo by lazy {
        CalendarRepositoryImpl(LunarMathAlgorithmDataSourceImpl(), StaticAssetDataSourceImpl())
    }
    private val getDaysInMonth by lazy { GetDaysInMonthUseCase(repo) }
    private val getDailyMetadata by lazy { GetDailyMetadataUseCase(repo) }

    private val sharedPrefs by lazy { GlobalContext.get().get<SharedPrefsManager>() }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val today = SolarDate.today()
        val days = getDaysInMonth(today.year, today.month)
            .getOrElse { emptyList() }
            .map { it.toUiModel() }
            .toImmutableList()
        val metadata = getDailyMetadata(today).getOrNull()

        val lunarYearLabel = metadata?.canChi?.let { "${it.canNam} ${it.chiNam}" }
        val lunarMonthLabel = metadata?.canChi?.let { "${it.canThang} ${it.chiThang}" }

        val showCanChi = sharedPrefs.showCanChiOnCell

        provideContent {
            CalendarWidgetContent(
                displayedMonth = today.month,
                displayedYear = today.year,
                lunarYearLabel = lunarYearLabel,
                lunarMonthLabel = lunarMonthLabel,
                days = days,
                showCanChiOnCell = showCanChi,
            )
        }
    }
}
