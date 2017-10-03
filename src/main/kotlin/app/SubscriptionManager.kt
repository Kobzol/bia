package app

import io.reactivex.disposables.Disposable

class SubscriptionManager
{
    private val subscriptions = ArrayList<Disposable>()

    fun add(subscription: Disposable)
    {
        this.subscriptions.add(subscription)
    }

    operator fun plusAssign(subscription: Disposable)
    {
        this.add(subscription)
    }

    fun unsubscribe()
    {
        this.subscriptions.forEach {
            it.dispose()
        }
        this.subscriptions.clear()
    }
}
