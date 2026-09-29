package di

import api.NewsApiClient
import org.example.project.MainViewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel

fun initKoin() {
    startKoin {
        printLogger(Level.DEBUG)
        modules(appModule)
    }
}

val commonModule = module {
    single<NewsApiClient>()
    viewModel<MainViewModel>()
}

expect val platformModule : Module

val appModule = listOf(commonModule,platformModule)