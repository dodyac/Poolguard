package com.acxdev.poolguardapps.repository

import com.acxdev.poolguardapps.model.Field
import java.util.*

object ChangelogNote {

    fun v1() = listOf(
        Field("Poolguard is now available!", "Track your mining rigs on your Android phones with beautiful UI.")
    )

    fun v1point01() = listOf(
        Field("New: Auto Update", "If your data want to be updated, you can enable auto update feature with 1 minute interval."),
        Field("New: Conflux Network (CFX)", "Conflux Network (CFX) on Nanopool is now available."),
        Locale("en", "IE").toField(),
        Field("New: Font", "Starting today, we're rolling out a new font on Poolguardᐩ, moving from Roboto to Manrope."),
        Field("New: Loading Animation", "We change loading animation with the animation of our icons."),
        Field("Fix: Coin Chart", "Fix crash when loading coin chart at homepage."),
        Field("Fix: Estimated Earnings", "Fix some estimated earnings not loading properly."),
        Field("Removed: Swipe Refresh", "Due to API limitations, we removed swipe refresh feature starting from this release."),
        Field("Removed: Notification Settings", "Notification settings are temporarily disabled due to some bugs."),
    )
    
    fun v1point02() = listOf(
        Field("Fix: Wallet", "Fix wallet not loading properly when first added.")
    )

    fun v1point03() = listOf(
        Field("Improve: Performance","Enjoy better performance with improved functionality."),
        Field("Improve: UI","UI refresh."),
        Locale("hi", "IN").toField(),
        Field("New: Chia Network (XCH)","Chia Network (XCH) on Flexpool is now available."),
        Field("Fix: Wallet","Fix wallet not updated automatically.")
    )

    fun v1point04() = listOf(
        Field("New: HeroMiners","HeroMiners is now available (Ergo & Haven Protocol)."),
        Locale("es", "AR").toField(),
        Locale("en", "AU").toField(),
        Locale("pt", "BR").toField(),
        Locale.CANADA.toField(),
        Locale.UK.toField(),
        Locale.KOREA.toField(),
        Locale("ms", "MY").toField(),
        Locale("tr", "TR").toField(),
        Field("Fix: Notification","Push notification is fixed now. Push allows you to receive an alert every time when the app is closed."),
        Field("Fix: Worker Chart","Fix worker chart not showing on Flexpool.")
    )

    fun v1point05() = listOf(
        Field("Fix: Hashrate Drop","Fix crash when Hashrate Drop Notification clicked."),
        Field("New: Polish Zloty (PLN)","Polish Zloty (PLN) currency is now available.")
    )

    fun v1point06() = listOf(
        Field("New: WoolyPooly", "WoolyPooly is now available (Ergo)."),
        Field("New: Conflux Network (CFX)", "Conflux Network (CFX) on HeroMiners is now available."),
        Locale.CHINA.toField(),
        Locale.JAPAN.toField(),
        Locale("es", "MX").toField(),
        Locale("es", "PE").toField(),
        Locale("ro", "RO").toField(),
        Locale("ru", "RU").toField(),
        Locale("en", "SG").toField(),
        Locale("th", "TH").toField(),
        Locale.TAIWAN.toField(),
        Field("Improve: UI", "UI refresh."),
        Field("Fix: Miner Chart", "Fix miner chart not showing on HeroMiners."),
        Field("Fix: Payment Chart", "Fix miner payment chart not showing when pinned to Dashboard.")
    )

    fun v1point07() = listOf(
        Pool.CRAZYPOOL.value.toField(),
        Pool.ZETPOOL.value.toField(),
        Field("New: HeroMiners Pool Coins","AEON, CCX, CTXC, QRL, RVN, TRTL and XMR on HeroMiners are now available."),
        Field("New: WoolyPooly Coins","AE, CFX, CTXC and ETC on WoolyPooly are now available."),
        Locale("bn", "BD").toField(),
        Locale("km", "KH").toField(),
        Locale("en", "HK").toField(),
        Locale("si", "LK").toField(),
        Locale("vi", "VN").toField(),
        Field("New: Profitability Calculator Coin","We added AE, AEON, CFX, CTXC, XHV, QRL Coin and pool fees option to the Profitability Calculator."),
        Field("Improve: Splash Indicator","We have updated our splash with a loading indicator to be more neutral when the app starts."),
        Field("Fix: Layout","Fixed the issue where scrolling horizontally should be held.")
    )

    fun v1point08() = listOf(
        Pool.TWOMINERS.value.toField(),
        Field("New: Coin Details","Added coin details (Nethash, difficulty, and latest blocks)."),
        Field("New: Sort Wallet List","Added a new feature to sort wallet list by name, pool, address and coin."),
        Field("New: Total Hashrate & Total Balance","Added a new feature that displays the total hashrate and total balance of all wallets."),
        Locale("ms", "BN").toField(),
        Locale("bg", "BG").toField(),
        Locale("es", "CO").toField(),
        Locale("es", "CR").toField(),
        Locale("en", "PH").toField(),
        Locale("en", "SE").toField(),
        Field("Improve: Performance","Improved performance when loading data from server."),
        Field("Fix: Flexpool Notification","Fixed worker notifications on Flexpool that might cause crashes."),
        Field("Fix: In-App Update","Fixed an In-App Update that might cause crashes."),
        Field("Fix: Current Hashrate","Fixed current worker hashrate that was incorrectly displaying values."),
        Field("Fix: WebView","Fixed a crash that could occur when opening WebView.")
    )

    fun v1point09() = listOf(
        Field("New: Ethpool", "Ethpool is now available."),
        Pool.LUCKPOOL.value.toField(),
        Field("New: Payments Notification","Activate it to get payment notifications from your mining results when it has been sent to your wallet."),
        Field("New: Add Wallet Shortcut","Ability to add wallet via shortcut. Simply long-press an app icon and select \"Add Wallet\"."),
        Field("New: Native Coin Option","Now, the wallet has an to display as native coin value or local price."),
        Locale("da", "DK").toField(),
        Locale("is", "IS").toField(),
        Locale("en", "JM").toField(),
        Locale("ne", "NP").toField(),
        Locale("no", "NO").toField(),
        Locale("en", "PK").toField(),
        Locale("de", "CH").toField(),
        Field("Improve: Performance","Improved performance when the app starts by 20%."),
        Field("Fix: Miner Hashrate Chart","Fixed miner hashrate chart that sometimes doesn't work properly.")
    )

    fun v1point10() = listOf(
        Pool.K1POOL.value.toField(),
        Pool.TWOMINERSSOLO.value.toField(),
        "SaturnPool".toField(),
        Pool.SOLOPOOL.value.toField(),
        "TWETH".toField(),
        Pool.WOOLYPOOLYSOLO.value.toField(),
        Field("New: Flypool Coin","Ergo (ERG) on Flypool is now available."),
        Field("New: WoolyPooly Coins","AION, BTG, ETH, FLUX, SERO, VEIL and VTC on WoolyPooly are now available."),
        Locale("hr", "HR").toField(),
        Locale("sr", "RS").toField(),
        Locale("en", "ZA").toField(),
        Locale("fr", "TN").toField(),
        Field("Fix: WoolyPooly Notification","Fixed worker and payment notifications on WoolyPooly that doesn't work properly.")
    )

    fun v1point11() = listOf(
        Field("New: Default Screen","Ability to set default screen when app launched."),
        Field("Improve: Native Coin Option","Now, currency type on Wallets are automatically based on your settings."),
        Field("Fix: Miner Chart", "Fixed miner chart not showing on 2Miners.")
    )

    fun v1point12() = listOf(
        Pool.BAIKALMINE.value.toField(),
        Pool.ETHO.value.toField(),
        Pool.MINEXMR.value.toField(),
        Pool.MYMINERS.value.toField(),
        Field("New: Use Default Browser","Now, you can use the default browser by enabling it in settings."),
        Locale("cs", "CZ").toField(),
        Locale("am", "ET").toField(),
        Locale("hu", "HU").toField(),
        Locale("kk", "KZ").toField(),
        Locale("en", "KE").toField(),
        Locale("es", "UY").toField(),
        Field("Fix: View on Browser","Fixed view on browser that doesn't work properly on certain pools."),
        Field("Fix: Notifications","Fixed notifications still running even though it was disabled in settings.")
    )

    fun v1point13() = listOf(
        Pool.COMINERS.value.toField(),
        "HLpool.org".toField(),
        Pool.RAVENMINER.value.toField(),
        "Upool.in".toField(),
        Field("New: Profitability Calculator for GPU (BETA)","Now you can calculate the mining rewards for cryptocurrencies mined using video cards (GPU)."),
        Field("New: HeroMiners Pool Coins","BEAM, DERO, ETC, ETH, XWP and UPX on HeroMiners are now available."),
        Field("New: WoolyPooly Coins","Ravencoin (RVN) on WoolyPooly is now available."),
        Locale("en", "AE").toField(),
        Locale("hy", "AM").toField(),
        Locale("pt", "AO").toField(),
        Locale("nl", "AW").toField(),
        Locale("ru", "BY").toField(),
        Locale("en", "BW").toField(),
        Locale("es", "CL").toField(),
        Locale("ro", "MD").toField(),
        Locale("es", "PY").toField(),
        Locale("en", "TZ").toField(),
        Field("Improve: Pin the Estimated Earnings Card","Now you can pin the estimated earnings card on the profitability calculator page, and it will run automatically when you launch the app."),
        Field("Improve: UI","Small UI change.")
    )

    fun v1point14() = listOf(
        Locale("bs", "BA").toField(),
        Locale("uk", "UA").toField(),
        Locale("ka", "GE").toField(),
        Locale("en", "GH").toField(),
        Locale("en", "NG").toField(),
        Locale("ar", "QA").toField(),
        Field("Fix: Nanopool Wallet","Fixed worker hashrate and Conflux Network (CFX) addres on Nanopool that doesn't work properly."),
        Field("Fix: HeroMiners Wallet","Fixed Ergo (ERG) Base URL on HeroMiners that doesn't work properly."),
    )

    fun v1point15() = listOf(
        Pool.EZIL.value.toField(),
        Pool.HIVEON.value.toField(),
        "MaxHash.org".toField(),
        Locale("ky","KG").toField(),
        Locale("ar","KW").toField(),
        Locale("ar","OM").toField(),
        Locale("es","PA").toField(),
        Locale("lg","UG").toField(),
        Field("New: Migrate Pool Option","We've added a function to migrate the pool on the wallet. Simply go to dashboard wallet > Menu > Migrate."),
        Field("New: Language Option","We’ve added a Chinese, Spanish and Russian languages to help localize the content."),
        Field("New: Wallet as Default Screen","Ability to add wallet as default screen. Simply go to settings and select your wallet."),
        Field("Fix: HeroMiners Notification","Fixed worker notification on HeroMiners that doesn't work properly."),
        Field("Improve: UI/UX and App Performance","Improved UI/UX and app performance."),
        Field("Removed: Hashrate Drop", "We removed Hashrate Drop feature starting from this release."),
        Field("Removed: Ethpool", "We removed Ethpool starting from this release."),
    )

    fun v1point16() = listOf(
        Field("New: Sync & restore wallet","Now you don't need to re-enter your wallet, just use the sync and restore feature in the account menu."),
        Field("Fix: Android 12 issue","Fixed the issue where the app force-closes on devices running Android 12."),
    )

    fun v1point17() = listOf(
        Pool.UNMINEABLE.value.toField(),
        Pool.MINERPOOL.value.toField(),
        Pool.ZETPOOLPPS.value.toField(),
        Field("New: In-App Update","Now you can check for updates and download them through the Poolguard app."),
        Field("New: Request Pool","Now you can send a request to list the pool you are using that's not listed in Poolguard."),
        Field("New: Forgot Password","Now you can send a request to reset your password."),
    )

    fun v1point18() = listOf(
        Pool.FLOCKPOOL.value.toField(),
        Pool.HEROMINERS.newCoinToField(Crypto.FLUX),
        Pool.FLEXPOOL.newCoinToField(Crypto.ETC),
        Field("New: Color Customization","Now you can change the accent color with several color options that have been provided in the Poolguard app."),
    )

    fun v1point19() = listOf(
        Pool.EZIL_ZIL.value.toField(),
        Field("New: Bahasa","Poolguard is now available in bahasa Indonesia."),
        Field("New: AMOLED Theme","Now you can change color into AMOLED theme on Poolguard app when dark mode is activated."),
        Field("Fix: Hashrate","Fixed miner hashrate summary that doesn't work on Ezil Pool."),
    )

    fun v1point20() = listOf(
        Field("New: Remove Ads","Removed all ads, now Poolguard is completely FREE!"),
        Field("New: EthereumPoW (ETHW","Added EthereumPoW (ETHW) after Ethereum migration."),
        Field("Removed: Inactive Pools","Removed some inactive pools."),
        Field("Improve: Performance","Enjoy better performance with improved functionality."),
        Field("Fix: Android 13","Fixed the function not working properly on Android 13."),
    )

    private fun Locale.toCurrencyAvailable(): String {
        val currency = Currency.getInstance(this)
        return "${currency.getDisplayName(Locale.ENGLISH)} (${currency.currencyCode}) currency is now available."
    }

    private fun Locale.toCurrencyNew(): String {
        val currency = Currency.getInstance(this)
        return "New: ${currency.getDisplayName(Locale.ENGLISH)} (${currency.currencyCode})"
    }

    private fun Locale.toField(): Field {
        return Field(toCurrencyNew(), toCurrencyAvailable())
    }

    private fun String.toField(): Field {
        return Field("New: $this", "$this is now available.")
    }

    private fun Pool.newCoinToField(crypto: Crypto): Field {
        return Field("New: ${this.value} Coin", "${crypto.fullName} (${crypto.name}) is now available.")
    }
}