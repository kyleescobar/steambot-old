package dev.steambot.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.ReferenceQueue
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Represents a JVM WeakReference wrapped object used for extension propertys.
 *
 * @param T
 * @property reference T?
 */
interface WeakReference<T> {

    val reference: T?

    fun clear()

}

interface WeakKeyedReference<K, V> : WeakReference<K> {

    val map: MutableMap<WeakKeyedReference<K, V>, V>

}

open class KotlinWeakReference<T>(value: T) : WeakReference<T> {

    internal val queue = ReferenceQueue<T>()

    private val wrappedWakeReference = java.lang.ref.WeakReference(value, queue)

    override val reference: T? get() = wrappedWakeReference.get()

    override fun clear() {
        wrappedWakeReference.clear()
    }

}

class KotlinWeakKeyedReference<K, V>(
    key: K,
    override val map: MutableMap<WeakKeyedReference<K, V>, V>
) : KotlinWeakReference<K>(key), WeakKeyedReference<K, V>  {

    /*
     * Use a global scoped coroutine to clear the queue of the reference.
     * This fixes an issue where the JVM fails to garbage collect stored references
     * after they are no longer needed.
     */
    init {
        GlobalScope.launch {
            @Suppress("BlockingMethodInNonBlockingContext")
            queue.remove()
            map.remove(this@KotlinWeakKeyedReference)
        }
    }
}

private fun <K, V, T : WeakReference<K>> MutableMap<T, V>.findWeakReferenceForKey(key: K) : T? {
    for((currentKey, _) in this) {
        if(currentKey.reference == key) {
            return currentKey
        }
    }

    return null
}

/**
 * The Mixin DSL global method for delegation.
 *
 * @param block [@kotlin.ExtensionFunctionType] Function1<PropertyMixin<T>, Unit>
 * @return PropertyMixin<T>
 */
fun <T, V> property(init: T? = null): ExtensionProperty<T, V> {
    return ExtensionProperty(init)
}

fun <T, V> nullableProperty(): NullablePropertyExtension<T, V> {
    return NullablePropertyExtension()
}

/**
 * Represents a Kotlin mixin'd property using kotlin extension methods by wrapper
 * the JVM weak referenced types with a kotlin coroutine backed garbage collector.
 *
 * Using this with a kotlin extension property getter and server will appear in kotlin source
 * code as a field which has been dynamically added to a class. This of course is not the case since there is no
 * injection happening.
 *
 * The kotlin coroutine GC provides this solution with almost zero overhead. However, this means this implementation
 * is not well suited for long-live instances of the runtime or for storing LARGE amounts of data since all data is stored
 * in memory when using extension properties.
 *
 * Also acts as a DSL for additional features such as data binding and bind aware object transforms.
 *
 * @param T
 */
class ExtensionProperty<T, V>(private val init: T?) : ReadWriteProperty<V, T> {

    private val map = mutableMapOf<WeakKeyedReference<Any, T>, T>()

    /**
     * Gets the mixin value from the reference map if it is initialized.
     *
     * @param thisRef Any
     * @param property KProperty<*>
     * @return T
     */
    override fun getValue(thisRef: V, property: KProperty<*>): T = thisRef?.let {
        map[map.findWeakReferenceForKey(thisRef)]
    } ?: (init
        ?: throw UninitializedPropertyAccessException("Unable to get the value of the mixin property as it has not been initialized."))

    /**
     * Sets and/or stores a value in the reference map  for a mixin property.
     *
     * @param thisRef Any
     * @param property KProperty<*>
     * @param value T
     */
    override fun setValue(thisRef: V, property: KProperty<*>, value: T) {

        val key: WeakKeyedReference<Any, T> = thisRef?.let {
            map.findWeakReferenceForKey(thisRef) ?: KotlinWeakKeyedReference(thisRef, map)
        } ?: return

        map[key] = value
    }
}

/**
 * Represents a Kotlin mixin'd property using kotlin extension methods by wrapper
 * the JVM weak referenced types with a kotlin coroutine backed garbage collector.
 *
 * Using this with a kotlin extension property getter and server will appear in kotlin source
 * code as a field which has been dynamically added to a class. This of course is not the case since there is no
 * injection happening.
 *
 * The kotlin coroutine GC provides this solution with almost zero overhead. However, this means this implementation
 * is not well suited for long-live instances of the runtime or for storing LARGE amounts of data since all data is stored
 * in memory when using extension propertys.
 *
 * Also acts as a DSL for additional features such as data binding and bind aware object transforms.
 *
 * @param T?
 */
class NullablePropertyExtension<T, V> : ReadWriteProperty<V, T?> {

    private val map = mutableMapOf<WeakKeyedReference<Any, T>, T>()

    /**
     * Gets the mixin value from the reference map if it is initialized.
     *
     * @param thisRef Any
     * @param property KProperty<*>
     * @return T
     */
    override fun getValue(thisRef: V, property: KProperty<*>): T? = thisRef?.let {
        map[map.findWeakReferenceForKey(thisRef)]
    } ?: throw UninitializedPropertyAccessException("Unable to get the value of the mixin property as it has not been initialized.")

    /**
     * Sets and/or stores a value in the reference map  for a mixin property.
     *
     * @param thisRef Any
     * @param property KProperty<*>
     * @param value T
     */
    override fun setValue(thisRef: V, property: KProperty<*>, value: T?) {

        val key: WeakKeyedReference<Any, T> = thisRef?.let {
            map.findWeakReferenceForKey(thisRef) ?: KotlinWeakKeyedReference(thisRef, map)
        } ?: return

        map[key] = value!!
    }
}