package com.acxdev.poolguardapps.repository

import com.acxdev.commonFunction.util.ext.emptyString
import com.acxdev.poolguardapps.common.base.BaseUrl
import com.acxdev.poolguardapps.model.coin.Coin
import com.acxdev.poolguardapps.repository.Crypto

object PoolCoinList {

    fun flockPoolCoin() = listOf(
        Coin(Crypto.RTM, BaseUrl.URL_FLOCKPOOL),
    )

    fun zetPoolPpsCoin() = listOf(
        Coin(Crypto.ETHW, BaseUrl.ZetPool.PPS.ETH),
    )

    fun minerPoolSoloCoin() = listOf(
        Coin(Crypto.FLUX, BaseUrl.MinerPool.Solo.FLUX),
        Coin(Crypto.RVN, BaseUrl.MinerPool.Solo.RVN),
    )

    fun minerPoolCoin() = listOf(
        Coin(Crypto.FLUX, BaseUrl.MinerPool.FLUX),
        Coin(Crypto.RVN, BaseUrl.MinerPool.RVN),
    )

    fun unMineableCoin() = listOf(
        Coin(Crypto.SHIB, BaseUrl.URL_UNMINEABLE),
    )

    fun ezilCoin() = listOf(
        Coin(Crypto.ETHW, BaseUrl.Ezil.STATS),
        Coin(Crypto.ETC, BaseUrl.Ezil.STATS),
    )

    fun hiveOnCoin() = listOf(
//        Coin(Crypto.ETC, BaseUrl.URL_HIVEON),
        Coin(Crypto.ETHW, BaseUrl.URL_HIVEON),
    )

    fun coMinersCoin() = listOf(
        Coin(Crypto.ETC, BaseUrl.CoMiners.ETC),
    )

    fun ravenPoolCoin() = listOf(
        Coin(Crypto.RVN, BaseUrl.URL_RAVEN_POOL)
    )
    
    fun myMinersCoin() = listOf(
        Coin(Crypto.CLO, BaseUrl.MyMiners.CLO),
        Coin(Crypto.ETC, BaseUrl.MyMiners.ETC),
        Coin(Crypto.EXP, BaseUrl.MyMiners.EXP),
    )
    
    fun ravenMinerCoin() = listOf(
        Coin(Crypto.RVN, BaseUrl.URL_RAVEN_MINER),
    )
    
    fun mineXmrCoin() = listOf(
        Coin(Crypto.XMR, BaseUrl.URL_MINEXMR),
    )
    
    fun myMinersSoloCoin() = listOf(
        Coin(Crypto.CLO, BaseUrl.MyMiners.Solo.CLO),
        Coin(Crypto.ETC, BaseUrl.MyMiners.Solo.ETC),
        Coin(Crypto.ETHW, BaseUrl.MyMiners.Solo.ETH),
        Coin(Crypto.EXP, BaseUrl.MyMiners.Solo.EXP),
    )
    
    fun baikalMineCoin() = listOf(
        Coin(Crypto.CLO, BaseUrl.BaikalMine.PPLNS.CLO),
        Coin(Crypto.ETC, BaseUrl.BaikalMine.PPLNS.ETC),
        Coin(Crypto.ETHW, BaseUrl.BaikalMine.PPLNS.ETHW),
    )
    
    fun baikalMineSoloCoin() = listOf(
        Coin(Crypto.CLO, BaseUrl.BaikalMine.Solo.CLO),
        Coin(Crypto.ETC, BaseUrl.BaikalMine.Solo.ETC),
    )
    
    fun baikalMinePpsCoin() = listOf(
        Coin(Crypto.ETC, BaseUrl.BaikalMine.PPS.ETC),
        Coin(Crypto.ETHW, BaseUrl.BaikalMine.PPS.ETHW),
    )

    fun k1PoolCoin() = listOf(
        Coin(Crypto.BTG, BaseUrl.K1Pool.BTG),
        Coin(Crypto.CLO, BaseUrl.K1Pool.CLO),
        Coin(Crypto.ERG, BaseUrl.K1Pool.ERG),
        Coin(Crypto.KMD, BaseUrl.K1Pool.KMD),
        Coin(Crypto.RVN, BaseUrl.K1Pool.RVN),
    )

    fun k1PoolSoloCoin() = listOf(
        Coin(Crypto.BTG, BaseUrl.K1Pool.Solo.BTG),
        Coin(Crypto.CLO, BaseUrl.K1Pool.Solo.CLO),
        Coin(Crypto.ERG, BaseUrl.K1Pool.Solo.ERG),
        Coin(Crypto.ETC, BaseUrl.K1Pool.Solo.ETC),
        Coin(Crypto.ETHW, BaseUrl.K1Pool.Solo.ETHW),
        Coin(Crypto.KMD, BaseUrl.K1Pool.Solo.KMD),
        Coin(Crypto.RVN, BaseUrl.K1Pool.Solo.RVN),
    )

    fun k1PoolRbppsCoin() = listOf(
        Coin(Crypto.ETC, BaseUrl.K1Pool.Rbpps.ETC),
        Coin(Crypto.ETHW, BaseUrl.K1Pool.Rbpps.ETHW)
    )

    fun twoMinersCoin() = listOf(
        Coin(Crypto.AE, BaseUrl.TwoMiners.AE),
        Coin(Crypto.BEAM, BaseUrl.TwoMiners.BEAM),
        Coin(Crypto.BTG, BaseUrl.TwoMiners.BTG),
        Coin(Crypto.CKB, BaseUrl.TwoMiners.CKB),
        Coin(Crypto.CLO, BaseUrl.TwoMiners.CLO),
        Coin(Crypto.CTXC, BaseUrl.TwoMiners.CTXC),
        Coin(Crypto.ERG, BaseUrl.TwoMiners.ERG),
        Coin(Crypto.ETC, BaseUrl.TwoMiners.ETC),
        Coin(Crypto.ETHW, BaseUrl.TwoMiners.ETH),
        Coin(Crypto.FIRO, BaseUrl.TwoMiners.FIRO),
        Coin(Crypto.FLUX, BaseUrl.TwoMiners.FLUX),
        Coin(Crypto.RVN, BaseUrl.TwoMiners.RVN),
        Coin(Crypto.XMR, BaseUrl.TwoMiners.XMR),
        Coin(Crypto.ZEC, BaseUrl.TwoMiners.ZEC),
    )

    fun twoMinersSoloCoin() = listOf(
        Coin(Crypto.AE, BaseUrl.TwoMiners.Solo.AE),
        Coin(Crypto.BEAM, BaseUrl.TwoMiners.Solo.BEAM),
        Coin(Crypto.BTG, BaseUrl.TwoMiners.Solo.BTG),
        Coin(Crypto.CKB, BaseUrl.TwoMiners.Solo.CKB),
        Coin(Crypto.CLO, BaseUrl.TwoMiners.Solo.CLO),
        Coin(Crypto.CTXC, BaseUrl.TwoMiners.Solo.CTXC),
        Coin(Crypto.ERG, BaseUrl.TwoMiners.Solo.ERG),
        Coin(Crypto.ETC, BaseUrl.TwoMiners.Solo.ETC),
        Coin(Crypto.ETHW, BaseUrl.TwoMiners.Solo.ETH),
        Coin(Crypto.FIRO, BaseUrl.TwoMiners.Solo.FIRO),
        Coin(Crypto.FLUX, BaseUrl.TwoMiners.Solo.FLUX),
        Coin(Crypto.RVN, BaseUrl.TwoMiners.Solo.RVN),
        Coin(Crypto.XMR, BaseUrl.TwoMiners.Solo.XMR),
        Coin(Crypto.ZEC, BaseUrl.TwoMiners.Solo.ZEC),
    )

    fun crazyPoolCoin() = listOf(
        Coin(Crypto.ETC, BaseUrl.CrazyPool.ETC),
        Coin(Crypto.ETHW, BaseUrl.CrazyPool.ETHW),
        Coin(Crypto.UBQ, BaseUrl.CrazyPool.UBQ),
    )

    fun soloPoolCoin() = listOf(
        Coin(Crypto.BEAM, BaseUrl.SoloPool.BEAM),
        Coin(Crypto.BTG, BaseUrl.SoloPool.BTG),
        Coin(Crypto.CLO, BaseUrl.SoloPool.CLO),
        Coin(Crypto.ETC, BaseUrl.SoloPool.ETC),
        Coin(Crypto.ETHW, BaseUrl.SoloPool.ETHW),
        Coin(Crypto.FIRO, BaseUrl.SoloPool.FIRO),
        Coin(Crypto.FLUX, BaseUrl.SoloPool.FLUX),
        Coin(Crypto.KMD, BaseUrl.SoloPool.KMD),
        Coin(Crypto.LTC, BaseUrl.SoloPool.LTC),
        Coin(Crypto.RVN, BaseUrl.SoloPool.RVN),
        Coin(Crypto.UBQ, BaseUrl.SoloPool.UBQ),
        Coin(Crypto.XMR, BaseUrl.SoloPool.XMR),
        Coin(Crypto.ZEC, BaseUrl.SoloPool.ZEC),
        Coin(Crypto.ZEN, BaseUrl.SoloPool.ZEN),
    )

    fun zetPoolCoin() = listOf(
        Coin(Crypto.ETC, BaseUrl.ZetPool.ETC),
        Coin(Crypto.ETHW, BaseUrl.ZetPool.ETH),
    )

    fun luckPoolCoin() = listOf(
        Coin(Crypto.VRSC, BaseUrl.LuckPool.VRSC),
    )

    fun woolyPoolyCoin() = listOf(
        Coin(Crypto.AE, BaseUrl.WoolyPooly.AE),
        Coin(Crypto.CFX, BaseUrl.WoolyPooly.CFX),
        Coin(Crypto.CTXC, BaseUrl.WoolyPooly.CTXC),
        Coin(Crypto.ERG, BaseUrl.WoolyPooly.ERG),
        Coin(Crypto.ETC, BaseUrl.WoolyPooly.ETC),
        Coin(Crypto.ETHW, BaseUrl.WoolyPooly.ETHW),
        Coin(Crypto.FLUX, BaseUrl.WoolyPooly.FLUX),
        Coin(Crypto.RVN, BaseUrl.WoolyPooly.RVN),
        Coin(Crypto.VEIL, BaseUrl.WoolyPooly.VEIL),
        Coin(Crypto.VTC, BaseUrl.WoolyPooly.VTC),
    )

    fun flexPoolCoin() = listOf(
        Coin(Crypto.ETC, BaseUrl.URL_FLEXPOOL),
        Coin(Crypto.XCH, BaseUrl.URL_FLEXPOOL),
    )

    fun flyPoolCoin() = listOf(
        Coin(Crypto.ERG, BaseUrl.FlyPool.ERG),
        Coin(Crypto.RVN, BaseUrl.FlyPool.RVN),
    )

    fun etherMineCoin() = listOf(
        Coin(Crypto.ETC, BaseUrl.EtherMine.ETC),
    )

    fun heroMinersCoin() = listOf(
        Coin(Crypto.BEAM, BaseUrl.HeroMiners.BEAM),
        Coin(Crypto.CCX, BaseUrl.HeroMiners.CCX),
        Coin(Crypto.CFX, BaseUrl.HeroMiners.CFX),
        Coin(Crypto.CTXC, BaseUrl.HeroMiners.CTXC),
        Coin(Crypto.ERG, BaseUrl.HeroMiners.ERG),
        Coin(Crypto.ETC, BaseUrl.HeroMiners.ETC),
        Coin(Crypto.ETHW, BaseUrl.HeroMiners.ETHW),
        Coin(Crypto.FLUX, BaseUrl.HeroMiners.FLUX),
        Coin(Crypto.QRL, BaseUrl.HeroMiners.QRL),
        Coin(Crypto.RVN, BaseUrl.HeroMiners.RVN),
        Coin(Crypto.TRTL, BaseUrl.HeroMiners.TRTL),
        Coin(Crypto.XHV, BaseUrl.HeroMiners.XHV),
        Coin(Crypto.XMR, BaseUrl.HeroMiners.XMR),
    )

    fun ethoClubPoolCoin() = listOf(
        Coin(Crypto.ETHO, BaseUrl.URL_ETHOCLUB),
    )

    fun nanoPoolCoin() = listOf(
        Coin(Crypto.CFX, BaseUrl.NanoPool.CFX),
        Coin(Crypto.ERG, BaseUrl.NanoPool.ERG),
        Coin(Crypto.ETC, BaseUrl.NanoPool.ETC),
        Coin(Crypto.ETHW, BaseUrl.NanoPool.ETHW),
        Coin(Crypto.RVN, BaseUrl.NanoPool.RVN),
        Coin(Crypto.XMR, BaseUrl.NanoPool.XMR),
        Coin(Crypto.ZEC, BaseUrl.NanoPool.ZEC),
    )

    fun profitabilityCoin() = listOf(
        Coin(Crypto.ETHW, emptyString()),
        Coin(Crypto.AE, emptyString()),
        Coin(Crypto.BEAM, emptyString()),
        Coin(Crypto.CFX, emptyString()),
        Coin(Crypto.CTXC, emptyString()),
        Coin(Crypto.ERG, emptyString()),
        Coin(Crypto.ETC, emptyString()),
        Coin(Crypto.QRL, emptyString()),
        Coin(Crypto.RVN, emptyString()),
        Coin(Crypto.TRTL, emptyString()),
        Coin(Crypto.XHV, emptyString()),
        Coin(Crypto.ZEC, emptyString()),
    )
}