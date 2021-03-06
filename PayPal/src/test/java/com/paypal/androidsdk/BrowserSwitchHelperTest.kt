package com.paypal.androidsdk

import android.net.Uri
import android.os.Build
import com.braintreepayments.api.models.PayPalUAT
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ Build.VERSION_CODES.P ])
class BrowserSwitchHelperTest {

    private lateinit var uat: PayPalUAT

    @Before
    fun beforeEach() {
        uat = mock()
    }

    @Test
    fun buildVerifyThreeDSecureUri() {
        val sut = BrowserSwitchHelper()

        val result = sut.buildVerifyThreeDSecureUri("https://contingency.com")
        val encodedRedirectUri = "com.paypal.demo.braintree%3A%2F%2Fx-callback-url%2Fpaypal-sdk%2Fcard-contingency"
        val expectedUrl = "https://contingency.com?redirect_uri=$encodedRedirectUri"
        assertEquals(result, Uri.parse(expectedUrl))
    }

    @Test
    fun buildPayPalCheckoutUri_whenEnvIsProduction_returnsProductionUrl() {
        val sut = BrowserSwitchHelper()
        whenever(uat.environment).thenReturn(PayPalUAT.Environment.PRODUCTION)

        val result = sut.buildPayPalCheckoutUri("sampleOrderId", uat)

        val encodedRedirectUri = "com.paypal.demo.braintree%3A%2F%2Fx-callback-url%2Fpaypal-sdk%2Fpaypal-checkout"
        val query = "token=sampleOrderId&redirect_uri=$encodedRedirectUri&native_xo=1"

        val expectedUrl = "https://www.paypal.com/checkoutnow?$query"
        assertEquals(result, Uri.parse(expectedUrl))
    }

    @Test
    fun buildPayPalCheckoutUri_whenEnvIsSandbox_returnsSandboxUrl() {
        val sut = BrowserSwitchHelper()
        whenever(uat.environment).thenReturn(PayPalUAT.Environment.SANDBOX)

        val result = sut.buildPayPalCheckoutUri("sampleOrderId", uat)

        val encodedRedirectUri = "com.paypal.demo.braintree%3A%2F%2Fx-callback-url%2Fpaypal-sdk%2Fpaypal-checkout"
        val query = "token=sampleOrderId&redirect_uri=$encodedRedirectUri&native_xo=1"

        val expectedUrl = "https://www.sandbox.paypal.com/checkoutnow?$query"
        assertEquals(result, Uri.parse(expectedUrl))
    }

    @Test
    fun buildPayPalCheckoutUri_whenEnvIsStaging_returnsStagingUrl() {
        val sut = BrowserSwitchHelper()
        whenever(uat.environment).thenReturn(PayPalUAT.Environment.STAGING)

        val result = sut.buildPayPalCheckoutUri("sampleOrderId", uat)

        val encodedRedirectUri = "com.paypal.demo.braintree%3A%2F%2Fx-callback-url%2Fpaypal-sdk%2Fpaypal-checkout"
        val query = "token=sampleOrderId&redirect_uri=$encodedRedirectUri&native_xo=1"

        val expectedUrl = "https://www.msmaster.qa.paypal.com/checkoutnow?$query"
        assertEquals(result, Uri.parse(expectedUrl))
    }
}