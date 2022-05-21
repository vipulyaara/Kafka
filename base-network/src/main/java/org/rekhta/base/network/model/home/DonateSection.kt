package org.rekhta.base.network.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DonateSection {
    @SerialName("TE")
    var tE: String? = null

    @SerialName("TH")
    var tH: String? = null

    @SerialName("TU")
    var tU: String? = null

    @SerialName("DE")
    var dE: String? = null

    @SerialName("DH")
    var dH: String? = null

    @SerialName("DU")
    var dU: String? = null

    @SerialName("FTE")
    var fTE: String? = null

    @SerialName("FTH")
    var fTH: String? = null

    @SerialName("FTU")
    var fTU: String? = null

    @SerialName("FU")
    var fU: String? = null

    @SerialName("PTE")
    var pTE: String? = null

    @SerialName("PTH")
    var pTH: String? = null

    @SerialName("PTU")
    var pTU: String? = null

    @SerialName("PU")
    var pU: String? = null

}
