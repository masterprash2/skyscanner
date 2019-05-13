package android.util

open class SparseArray<E> {

    private val mHashMap: HashMap<Int, E> = HashMap()

    fun put(key: Int, value: E) {
        mHashMap[key] = value
    }

    operator fun get(key: Int): E? {
        return mHashMap[key]
    }

    fun first() : E? {
        return mHashMap[0]
    }

    fun size() : Int {
        return mHashMap.values.size
    }
}