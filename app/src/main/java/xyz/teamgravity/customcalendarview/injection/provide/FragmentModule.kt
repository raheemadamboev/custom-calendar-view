package xyz.teamgravity.customcalendarview.injection.provide

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import xyz.teamgravity.customcalendarview.core.util.Helper
import xyz.teamgravity.customcalendarview.presentation.adapter.CalendarAdapter
import xyz.teamgravity.customcalendarview.presentation.adapter.CalendarHeaderAdapter
import xyz.teamgravity.customcalendarview.presentation.adapter.DataAdapter
import java.time.LocalDate

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    @FragmentScoped
    fun provideDataDiff(): DataAdapter.DataDiff = DataAdapter.DataDiff()

    @Provides
    @FragmentScoped
    fun provideDataAdapter(dataDiff: DataAdapter.DataDiff): DataAdapter = DataAdapter(dataDiff)

    @Provides
    @FragmentScoped
    fun provideCalendarHeaderAdapter(): CalendarHeaderAdapter = CalendarHeaderAdapter(Helper.daysOfWeekFromLocale())

    @Provides
    @FragmentScoped
    fun provideCalendarAdapter(): CalendarAdapter = CalendarAdapter(LocalDate.now())
}