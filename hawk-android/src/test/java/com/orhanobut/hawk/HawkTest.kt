package com.orhanobut.hawk

import com.google.common.truth.Truth.assertThat
import com.orhanobut.hawk.Hawk.db
import junit.framework.Assert.fail
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks

class HawkTest {

    @Mock
    private lateinit var hawkFacade: HawkDB

    @Before
    fun setup() {
        initMocks(this)
        Hawk.INSTANCES[Hawk.DEFAULT_DB_TAG] = hawkFacade
    }

    @After
    fun tearDown() {
        db().destroy()
    }

    @Test
    fun testInit() {
        try {
            Hawk.init(null)
            fail("context should not be null")
        } catch (e: Exception) {
            assertThat(e).hasMessage("Context should not be null")
        }

    }

    @Test
    fun put() {
        db().put("key", "value")

        verify(hawkFacade).put("key", "value")
    }

    @Test
    fun get() {
        db().get<Any>("key")

        verify(hawkFacade).get<Any>("key")
    }

    @Test
    fun getWithDefault() {
        db().get("key", "default")

        verify(hawkFacade).get("key", "default")
    }

    @Test
    fun count() {
        db().count()

        verify(hawkFacade).count()
    }

    @Test
    fun deleteAll() {
        db().deleteAll()

        verify(hawkFacade).deleteAll()
    }

    @Test
    fun delete() {
        db().delete("key")

        verify(hawkFacade).delete("key")
    }

    @Test
    fun contains() {
        db().contains("key")

        verify(hawkFacade).contains("key")
    }

    @Test
    fun isBuilt() {
        db().isBuilt

        verify(hawkFacade).isBuilt
    }

}
