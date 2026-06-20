package com.decoutkhanqindev.lich_viet_loc_phat.di

import android.content.Context
import com.decoutkhanqindev.lich_viet_loc_phat.data.repository.CalendarRepositoryImpl
import com.decoutkhanqindev.lich_viet_loc_phat.data.repository.SettingsRepositoryImpl
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.lunar_math_algorithm.LunarMathAlgorithmDataSource
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.lunar_math_algorithm.LunarMathAlgorithmDataSourceImpl
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset.StaticAssetDataSource
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset.StaticAssetDataSourceImpl
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.CalendarRepository
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.SettingsRepository
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetCalendarWidgetEnabledUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDailyMetadataUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDaysInMonthUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetHourlyAuspiciousnessUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetShowCanChiOnCellUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetSolarTermUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.ObserveShowCanChiOnCellUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.SetCalendarWidgetEnabledUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.SetShowCanChiOnCellUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.CalendarViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.SettingsViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.today.TodayViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { androidContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE) }

    single<LunarMathAlgorithmDataSource> { LunarMathAlgorithmDataSourceImpl() }
    single<StaticAssetDataSource> { StaticAssetDataSourceImpl() }
    single { NetworkManager(androidContext()) }
    single { AdsManager() }

    single<CalendarRepository> { CalendarRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    factory { GetDailyMetadataUseCase(get()) }
    factory { GetDaysInMonthUseCase(get()) }
    factory { GetHourlyAuspiciousnessUseCase(get()) }
    factory { GetSolarTermUseCase(get()) }
    factory { GetShowCanChiOnCellUseCase(get()) }
    factory { SetShowCanChiOnCellUseCase(get()) }
    factory { ObserveShowCanChiOnCellUseCase(get()) }
    factory { GetCalendarWidgetEnabledUseCase(get()) }
    factory { SetCalendarWidgetEnabledUseCase(get()) }

    viewModel { TodayViewModel(get()) }
    viewModel { CalendarViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get(), androidApplication()) }
}
