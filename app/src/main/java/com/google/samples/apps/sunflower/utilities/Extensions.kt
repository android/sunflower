package com.google.samples.apps.sunflower.utilities

import androidx.fragment.app.Fragment
import androidx.lifecycle.*

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
        provider: ViewModelProvider.Factory
) = ViewModelProviders.of(this, provider).get(VM::class.java)

inline fun <T> LiveData<T>.observe(
        owner: LifecycleOwner,
        crossinline observer: (T?) -> Unit
) {
    observe(owner, Observer<T> { v -> observer(v) })
}

inline fun <X, Y> LiveData<X>.map(crossinline transformer: (X) -> Y): LiveData<Y> =
        Transformations.map(this) { transformer(it) }

inline fun <X, Y> LiveData<X>.switchMap(crossinline transformer: (X) -> LiveData<Y>): LiveData<Y> =
        Transformations.switchMap(this) { transformer(it) }