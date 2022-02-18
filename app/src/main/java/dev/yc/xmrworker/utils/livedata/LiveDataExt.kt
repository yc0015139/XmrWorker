package dev.yc.xmrworker.utils.livedata

import androidx.lifecycle.MutableLiveData


typealias SingleEvent = Event<Unit>

fun MutableLiveData<SingleEvent>.invoke() {
    this.value = Event(Unit)
}