package com.orhanobut.hawk

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConcealEncryptionTest {

  private lateinit var encryption: Encryption

  @Before fun setup() {
      encryption = ConcealEncryption(InstrumentationRegistry.getInstrumentation().targetContext)
  }

  @Test fun init() {
    assertThat(encryption.init()).isTrue()
  }

  @Test fun testEncryptAndDecrypt() {
    val key = "key"
    val value = "value"

    val cipherText = encryption.encrypt(key, value)

    val plainValue = encryption.decrypt(key, cipherText)

    assertThat(plainValue).isEqualTo(value)
  }
}