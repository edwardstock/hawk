package com.orhanobut.hawk

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.orhanobut.hawk.Hawk.db
import org.junit.Test

class HawkRealTest {

    init {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        Hawk.init(ctx)
                .setLogger { println("Hawk $it") }
                .build()

    }

    @Test(timeout = 100000L)
    fun testPut() {
        Truth.assertThat(db().count()).isEqualTo(0)

        db().put("t1", "v1")
        Truth.assertThat(db().get<String?>("t1")).isEqualTo("v1")

        db().delete("t1")
        Truth.assertThat(db().contains("t1")).isFalse()

        for (x in 1..100) {
            db().put("key_$x", "val_$x")
        }

        Truth.assertThat(db().count()).isEqualTo(100)

        Truth.assertThat(db().get<String?>("key_45")).isEqualTo("val_45")
        db().deleteAll()

        Truth.assertThat(db().count()).isEqualTo(0)
    }

    @Test(timeout = 100000L)
    fun testPutBatch() {
        Truth.assertThat(db().count()).isEqualTo(0)

        val batch = StorageBatch()

        for (x in 1..1000) {
            batch.put("key_$x", "val_$x")
        }
        db().batch(batch)

        Truth.assertThat(db().get<String?>("key_345")).isEqualTo("val_345")

        Truth.assertThat(db().count()).isEqualTo(1000)


        db().deleteAll()

        Truth.assertThat(db().count()).isEqualTo(0)
        db().deleteAll()
    }
}