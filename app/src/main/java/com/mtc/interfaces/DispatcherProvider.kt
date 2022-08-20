package com.mtc.interfaces

import kotlinx.coroutines.CoroutineDispatcher

/**
 *
 * [DispatcherProvider] provides the dispatcher to make sure test case should run properly
 *
 * @author GWL
 */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}