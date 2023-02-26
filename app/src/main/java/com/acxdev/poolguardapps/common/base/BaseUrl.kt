package com.acxdev.poolguardapps.common.base

import com.acxdev.poolguardapps.model.PaymentList
import com.acxdev.poolguardapps.model.Wallet
import com.acxdev.poolguardapps.repository.Crypto

object BaseUrl {
    const val URL_POOLGUARD_APP = "https://poolguard.eagercodes.com/api/"

    const val URL_CRYPTOCOMPARE = "https://min-api.cryptocompare.com/data/"
    const val URL_COINSTATS = "https://api.coinstats.app/public/v1/"
    const val URL_WHATTOMINE = "https://whattomine.com/"
    const val URL_CHIA_NETWORK = "https://api.chiaprofitability.com/"

    const val URL_FLEXPOOL = "https://api.flexpool.io/v2/"
    const val URL_ETHOCLUB = "https://etho.club/api/"
    const val URL_MINEXMR = "https://minexmr.com/api/"
    const val URL_RAVEN_MINER = "https://www.ravenminer.com/api/v1/"
    const val URL_RAVEN_POOL = "https://api.ravenpool.ninja/"
    const val URL_HIVEON = "https://hiveon.net/api/v1/"
    const val URL_UNMINEABLE = "https://api.unminable.com/v4/"
    const val URL_FLOCKPOOL = "https://flockpool.com/api/"

    const val URL_ICONS8 = "https://icons8.com/"

    object Apps {
        const val POOLGUARD_DISCORD = "https://discord.gg/JRTPtvbWKe"
        const val POOLGUARD_FACEBOOK = "https://facebook.com/PoolguardApp/"
        const val REQUEST_POOL = "https://docs.google.com/forms/d/e/1FAIpQLSeAoFrzEvXOhkzk7Tgp753tgKmEJH3IvsBUu8sgDPABfJYd2Q/viewform?usp=sf_link"
        const val HELP_TRANSLATE = "https://docs.google.com/forms/d/e/1FAIpQLSfJektPs2ycnXykhbwdBh17FBsyQjFmLy2MgykGN6EOqndLgg/viewform?usp=sf_link"
    }

    fun Wallet.explorer(list: PaymentList): String {
        val hash = list.txHash ?: list.kernelId
        return when(symbol){
            Crypto.RTM.name -> "https://explorer.raptoreum.com/tx/$hash"
            Crypto.ETC.name -> "https://blockscout.com/etc/mainnet/tx/$hash/internal-transactions"
            Crypto.ERG.name -> "https://explorer.ergoplatform.com/en/transactions/$hash"
            Crypto.RVN.name -> "https://ravencoin.network/tx/$hash"
            Crypto.BEAM.name -> "https://mainnet-explorer.beam.mw/explorer/search/?q=$hash"
            Crypto.ZEC.name -> "https://zcha.in/transactions/$hash"
            Crypto.AE.name -> "https://aeknow.org/block/transaction/$hash"
            Crypto.BTG.name -> "https://explorer.bitcoingold.org/insight/tx/$hash"
            Crypto.CKB.name -> "https://explorer.nervos.org/transaction/$hash"
            Crypto.CLO.name -> "https://explorer.callisto.network/tx/$hash"
            Crypto.CTXC.name -> "https://cerebro.cortexlabs.ai/#/tx/$hash"
            Crypto.EXP.name -> "https://explorer.expanse.tech/tx/$hash"
            Crypto.FIRO.name -> "https://explorer.firo.org/tx/$hash"
            Crypto.FLUX.name -> "https://explorer.runonflux.io/tx/$hash"
            Crypto.XMR.name -> "https://xmrchain.net/tx/$hash"
            Crypto.CFX.name -> "https://confluxscan.io/transaction/$hash"
            Crypto.TRTL.name -> "https://explorer.turtlecoin.lol/transaction.html?hash=$hash"
            Crypto.XHV.name -> "https://explorer.havenprotocol.org/tx/$hash"
            Crypto.QRL.name -> "https://explorer.theqrl.org/tx/$hash"
            Crypto.CCX.name -> "https://explorer.conceal.network/index.html?hash=$hash"
            Crypto.VRSC.name -> "https://explorer.verus.io/tx/$hash"
            Crypto.UBQ.name -> "https://ubiqscan.io/tx/$hash"
            Crypto.ETHO.name -> "https://blocks.ethoprotocol.com/tx/$hash"
            Crypto.LTC.name -> "https://insight.litecore.io/tx/$hash"
            Crypto.KMD.name -> "https://kmdexplorer.io/tx/$hash"
            Crypto.VEIL.name -> "https://explorer.veil-project.com/tx/$hash"
            Crypto.VTC.name -> "https://chainz.cryptoid.info/vtc/tx.dws?$hash.htm"
            Crypto.XCH.name -> "https://www.chiaexplorer.com/blockchain/coin/$hash"
            else -> "https://etherscan.io/tx/$hash"
        }
    }

    object MinerPool {
        const val FLUX = "https://flux.minerpool.org/api/"
        const val RVN = "https://rvn.minerpool.org/api/"

        object Solo {
            const val FLUX = "https://solo-flux.minerpool.org/api/"
            const val RVN = "https://solo-rvn.minerpool.org/api/"
        }
    }

    object Ezil {
        const val STATS = "https://stats.ezil.me/"
        const val BILLING = "https://billing.ezil.me/"
    }

    object CoMiners {
        const val ETC = "https://etc.cominers.org/api/"
    }

    object MyMiners {
        const val CLO = "https://clo.myminers.org/api/"
        const val ETC = "https://etc.myminers.org/api/"
        const val EXP = "https://exp.myminers.org/api/"

        object Solo {
            const val CLO = "https://solo-clo.myminers.org/api/"
            const val ETC = "https://solo-etc.myminers.org/api/"
            const val ETH = "https://solo-eth.myminers.org/api/"
            const val EXP = "https://solo-exp.myminers.org/api/"
        }
    }

    object BaikalMine {
        object PPS {
            const val ETHW = "https://ethw-pps.baikalmine.com/api/"
            const val ETC = "https://etc-pps.baikalmine.com/api/"
        }
        object Solo {
            const val CLO = "https://clo-solo.baikalmine.com/api/"
            const val ETC = "https://etc-solo.baikalmine.com/api/"
        }
        object PPLNS {
            const val CLO = "https://clo.baikalmine.com/api/"
            const val ETC = "https://etc.baikalmine.com/api/"
            const val ETHW = "https://ethw.baikalmine.com/api/"
        }
    }

    object SoloPool {
        const val BEAM = "https://beam.solopool.org/api/"
        const val BTG = "https://btg.solopool.org/api/"
        const val CLO = "https://clo.solopool.org/api/"
        const val ETC = "https://etc.solopool.org/api/"
        const val ETHW = "https://ethw.solopool.org/api/"
        const val FIRO = "https://firo.solopool.org/api/"
        const val FLUX = "https://flux.solopool.org/api/"
        const val KMD = "https://kmd.solopool.org/api/"
        const val LTC = "https://ltc.solopool.org/api/"
        const val RVN = "https://rvn.solopool.org/api/"
        const val UBQ = "https://ubq.solopool.org/api/"
        const val XMR = "https://xmr.solopool.org/api/"
        const val ZEC = "https://zec.solopool.org/api/"
        const val ZEN = "https://zen.solopool.org/api/"
    }

    object LuckPool {
        const val VRSC = "https://luckpool.net/verus/"
    }

    object CrazyPool {
        const val ETHW = "https://ethw.crazypool.org/api/"
        const val ETC = "https://etc.crazypool.org/api/"
        const val UBQ = "https://ubq.crazypool.org/api/"
    }

    object K1Pool {
        const val BTG = "https://k1pool.com/api/miner/btg/"
        const val CLO = "https://k1pool.com/api/miner/clo/"
        const val ERG = "https://k1pool.com/api/miner/erg/"
        const val KMD = "https://k1pool.com/api/miner/kmd/"
        const val RVN = "https://k1pool.com/api/miner/rvn/"

        object Solo {
            const val BTG = "https://k1pool.com/api/miner/btgsolo/"
            const val CLO = "https://k1pool.com/api/miner/closolo/"
            const val ERG = "https://k1pool.com/api/miner/ergsolo/"
            const val ETC = "https://k1pool.com/api/miner/etcsolo/"
            const val ETHW = "https://k1pool.com/api/miner/ethwsolo/"
            const val KMD = "https://k1pool.com/api/miner/kmdsolo/"
            const val RVN = "https://k1pool.com/api/miner/rvnsolo/"
        }

        object Rbpps {
            const val ETC = "https://k1pool.com/api/miner/etc/"
            const val ETHW = "https://k1pool.com/api/miner/ethw/"
        }
    }

    object EtherMine {
        const val ETC = "https://api-etc.ethermine.org/"
    }

    object FlyPool {
        const val ERG = "https://api-ergo.flypool.org/"
        const val RVN = "https://api-ravencoin.flypool.org/"
    }

    object HeroMiners {
        const val ERG = "https://ergo.herominers.com/api/"
        const val CCX = "https://conceal.herominers.com/api/"
        const val CFX = "https://conflux.herominers.com/api/"
        const val CTXC = "https://cortex.herominers.com/api/"
        const val XHV = "https://haven.herominers.com/api/"
        const val RVN = "https://ravencoin.herominers.com/api/"
        const val TRTL = "https://turtlecoin.herominers.com/api/"
        const val XMR = "https://monero.herominers.com/api/"
        const val QRL = "https://qrl.herominers.com/api/"
        const val BEAM = "https://beam.herominers.com/api/"
        const val ETC = "https://etc.herominers.com/api/"
        const val ETHW = "https://ethw.herominers.com/api/"
        const val FLUX = "https://flux.herominers.com/api/"
    }

    object NanoPool {
        const val ETHW = "https://api.nanopool.org/v1/eth/"
        const val ETC = "https://api.nanopool.org/v1/etc/"
        const val ZEC = "https://api.nanopool.org/v1/zec/"
        const val XMR = "https://api.nanopool.org/v1/xmr/"
        const val RVN = "https://api.nanopool.org/v1/rvn/"
        const val CFX = "https://api.nanopool.org/v1/cfx/"
        const val ERG = "https://api.nanopool.org/v1/ergo/"
    }

    object TwoMiners {
        const val AE = "https://ae.2miners.com/api/"
        const val CKB = "https://ckb.2miners.com/api/"
        const val CLO = "https://clo.2miners.com/api/"
        const val CTXC = "https://ctxc.2miners.com/api/"
        const val FIRO = "https://firo.2miners.com/api/"
        const val BEAM = "https://beam.2miners.com/api/"
        const val RVN = "https://rvn.2miners.com/api/"
        const val ERG = "https://erg.2miners.com/api/"
        const val ETH = "https://ethw.2miners.com/api/"
        const val ETC = "https://etc.2miners.com/api/"
        const val XMR = "https://xmr.2miners.com/api/"
        const val BTG = "https://btg.2miners.com/api/"
        const val ZEC = "https://zec.2miners.com/api/"
        const val FLUX = "https://flux.2miners.com/api/"

        object Solo {
            const val AE = "https://solo-ae.2miners.com/api/"
            const val CKB = "https://solo-ckb.2miners.com/api/"
            const val CLO = "https://solo-clo.2miners.com/api/"
            const val CTXC = "https://solo-ctxc.2miners.com/api/"
            const val FIRO = "https://solo-firo.2miners.com/api/"
            const val BEAM = "https://solo-beam.2miners.com/api/"
            const val RVN = "https://solo-rvn.2miners.com/api/"
            const val ERG = "https://solo-erg.2miners.com/api/"
            const val ETH = "https://solo-ethw.2miners.com/api/"
            const val ETC = "https://solo-etc.2miners.com/api/"
            const val XMR = "https://solo-xmr.2miners.com/api/"
            const val BTG = "https://solo-btg.2miners.com/api/"
            const val ZEC = "https://solo-zec.2miners.com/api/"
            const val FLUX = "https://solo-flux.2miners.com/api/"
        }
    }

    object WoolyPooly {
        const val ERG = "https://api.woolypooly.com/api/ergo-1/"
        const val ETC = "https://api.woolypooly.com/api/etc-1/"
        const val ETHW = "https://api.woolypooly.com/api/ethw-1/"
        const val CFX = "https://api.woolypooly.com/api/cfx-1/"
        const val CTXC = "https://api.woolypooly.com/api/cortex-1/"
        const val AE = "https://api.woolypooly.com/api/aeternity-1/"
        const val VEIL = "https://api.woolypooly.com/api/veil-1/"
        const val VTC = "https://api.woolypooly.com/api/vtc-1/"
        const val FLUX = "https://api.woolypooly.com/api/zel-1/"
        const val RVN = "https://api.woolypooly.com/api/raven-1/"
    }

    object ZetPool {
        const val ETH = "https://zetpool.org/eth/api/"
        const val ETC = "https://zetpool.org/etc/api/"

        object PPS {
            const val ETH = "https://zetpool.org/eth-pps/api/"
        }
    }
}