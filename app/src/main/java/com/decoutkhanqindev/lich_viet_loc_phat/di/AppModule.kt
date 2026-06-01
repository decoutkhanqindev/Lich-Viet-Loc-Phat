package com.decoutkhanqindev.lich_viet_loc_phat.di

import android.content.Context
import com.decoutkhanqindev.lich_viet_loc_phat.data.repository.CalendarRepositoryImpl
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.lunar_math_algorithm.LunarMathAlgorithmDataSource
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.lunar_math_algorithm.LunarMathAlgorithmDataSourceImpl
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset.StaticAssetDataSource
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset.StaticAssetDataSourceImpl
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.CalendarRepository
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.CalculateCanChiUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.ConvertLunarToSolarUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.ConvertSolarToLunarUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDailyMetadataUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDaysInMonthUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetHourlyAuspiciousnessUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetSolarTermUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.CalendarViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter.ConverterViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.SettingsViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today.TodayViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // --- Settings persistence ---
    single { androidContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE) }

    // --- Screen ViewModels ---
    viewModel { (initialDate: SolarDate?) -> TodayViewModel(get(), initialDate) }
    viewModel { CalendarViewModel(get(), get()) }
    viewModel { ConverterViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get()) }

    // --- Data Sources ---
    single<LunarMathAlgorithmDataSource> { LunarMathAlgorithmDataSourceImpl() }
    single<StaticAssetDataSource> { StaticAssetDataSourceImpl() }

    // --- Repository ---
    single<CalendarRepository> { CalendarRepositoryImpl(get(), get()) }

    // --- Use Cases ---
    factory { GetDailyMetadataUseCase(get()) }
    factory { GetDaysInMonthUseCase(get()) }
    factory { ConvertSolarToLunarUseCase(get()) }
    factory { ConvertLunarToSolarUseCase(get()) }
    factory { CalculateCanChiUseCase(get()) }
    factory { GetHourlyAuspiciousnessUseCase(get()) }
    factory { GetSolarTermUseCase(get()) }
}
