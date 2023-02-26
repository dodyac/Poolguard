package com.acxdev.poolguardapps.repository

enum class MyAlgorithm(
    val unit: String,
    val isGpu: Boolean
) {
    //GPU
    Ethash(UnitHash.MH.value, true),
    Ethash4G(UnitHash.MH.value, true),
    Autolykos(UnitHash.MH.value, true),
    KawPow(UnitHash.MH.value, true),
    Zhash(UnitHash.H.value, true),
    CNHeavy(UnitHash.H.value, true),
    CNGPU(UnitHash.H.value, true),
    CryptoNightR(UnitHash.H.value, true),
    CNFast(UnitHash.H.value, true),
    Aion(UnitHash.H.value, true),
    CuckooCycle(UnitHash.H.value, true),
    Cuckarood29(UnitHash.H.value, true),
    Cuckatoo31(UnitHash.H.value, true),
    Cuckatoo32(UnitHash.H.value, true),
    Beam(UnitHash.H.value, true),
    RandomX(UnitHash.H.value, true),
    NeoScrypt(UnitHash.KH.value, true),
    Octopus(UnitHash.MH.value, true),
    EquihashZero(UnitHash.H.value, true),
    ZelHash(UnitHash.H.value, true),
    ProgPow(UnitHash.MH.value, true),
    X25X(UnitHash.MH.value, true),
    MTP(UnitHash.MH.value, true),
    VertHash(UnitHash.MH.value, true),

    //ASIC
    SHA256(UnitHash.GH.value, false),
    Scrypt(UnitHash.MH.value, false),
    X11(UnitHash.MH.value, false),
    Sia(UnitHash.GH.value, false),
    Quark(UnitHash.MH.value, false),
    Qubit(UnitHash.MH.value, false),
    MyrGroestl(UnitHash.GH.value, false),
    Skein(UnitHash.GH.value, false),
    LBRY(UnitHash.GH.value, false),
    Blake14r(UnitHash.GH.value, false),
    X11Gost(UnitHash.GH.value, false),
    CryptoNight(UnitHash.KH.value, false),
    Equihash(UnitHash.KH.value, false),
    Lyra2REv2(UnitHash.GH.value, false),
    BCD(UnitHash.MH.value, false),
    Lyra2z(UnitHash.MH.value, false),
    PHI1612(UnitHash.MH.value, false),
    Keccak(UnitHash.GH.value, false),
    Groestl(UnitHash.GH.value, false),
    Eaglesong(UnitHash.GH.value, false),
    Tensority(UnitHash.KH.value, false),

    //Unknown
    CuckooCycleCortex(UnitHash.H.value, false),
    ChukwaV2(UnitHash.H.value, false),
    K12(UnitHash.H.value, false),
    VerusHash(UnitHash.H.value, false),
    AstroBWT(UnitHash.H.value, false),
    CryptoNightDefyx(UnitHash.H.value, false),
    CryptoNightUPX(UnitHash.H.value, false),
    Consensus(UnitHash.H.value, false),
    GhostRider(UnitHash.H.value, false),
}