package org.rekhta.base.network.model.tab

import java.io.Serializable

/**
 * Authored by vipulkumar on 14/12/17.
 */
const val contentTypeGhazal = "43d60a15-0b49-4caf-8b74-0fcdddeb9f83"
const val contentTypeNazm = "c54c4d8b-7e18-4f70-8312-e1c2cc028b0b"
const val contentTypeSher = "f722d5dc-45da-41ec-a439-900df702a3d6"
const val contentTypeFavorites = "favorites"
const val contentTypeAudios = "audios"
const val contentTypeImageShayri = "image"
const val contentTypePoet = "content_type_poet"
const val contentTypeProfile = "content_type_profile"
const val contentTypeRubai = "94ec8d36-9c2f-4847-a577-3fe31ddbbc62"
const val contentTypeMarsiya = "c24bb39f-4650-4262-884c-ef070c2e4dea"
const val contentTypeSehra = "74737934-d5a0-49fe-98eb-510b041878e6"
const val contentTypeUnpublishedGhazals = "bdab96d8-cb3e-4601-9f2d-6db0981e9795"
const val contentTypeQasida = "6997e0a6-afb6-4c39-9ae2-d88d9433392d"
const val contentTypeQita = "a13c352e-03d3-43fe-ace0-f64918f6d28a"
const val contentTypeSalam = "40062403-dc01-4dde-8a9a-11a2e93edb5f"
const val contentTypeMukhammas = "6f7d1a2c-2198-48dc-88b0-344fc1e3d081"
const val contentTypeLatifa = "7265679c-f13e-499f-a833-9f432da7e07d"
const val contentTypeMasnavi = "b1f7e08b-3fc7-4318-9372-9760cbab8c56"
const val contentTypeUnpublishedSher = "035fbead-d1c1-488f-9512-9ed5c95d1b6e"
const val contentTypeShortStory = "cfe3362b-1847-4abf-a480-00677a448bd6"
const val contentTypemicrofiction = "48dc4841-5454-49de-b08c-6447e6840c13"
const val contentTypedrama = "db6c1b42-5ce0-4702-8ad2-722f1952f53e"
const val contentTypekhaka = "6b02f90d-e18c-4c41-82be-793b6decab72"
const val contentTypetanzmizah = "ea9d2fe5-a0eb-466d-9c1f-70e62f141f96"
const val contentTypearticle = "534d1f24-7faf-4c34-a116-f3a1026fb5f3"
const val contentTypenovel = "6ca3bdb0-c605-44eb-96b2-0f7158dbf58b"
const val contentTypetranslation = "b87bb313-f4bb-41d5-9349-98b45ef661ad"

val contentTypes = mapOf(
    contentTypeGhazal to "Ghazal",
    contentTypeNazm to "Nazm",
    contentTypeSher to "Sher",
    contentTypePoet to "Ghazal"
)

data class TabData(
    val title: String?,
    val contentType: String?,
    val targetId: String?,
    val startPosition: Int = 0,
    val poetId: String? = null,
    val ghazalCount: Int = 0,
    val nazmCount: Int = 0,
    val poetDescription: String = ""
) : Serializable

enum class TabType {
    Ghazal,
    Nazm,
    Poet
}

fun getTabDataFromTabType(tabType: TabType): Array<TabData> {
    return when (tabType) {
        TabType.Ghazal -> ghazalTabData()
        TabType.Nazm -> nazmTabData()
        TabType.Poet -> poetTabData()
    }
}

fun ghazalTabData() = arrayOf(
    TabData("Top 100", contentTypeGhazal, "60e03626-6a76-481c-b4d6-cdd6173da417"),
    TabData("Humour/Satire", contentTypeGhazal, "7DF9047A-D6B7-4CB0-8180-C29F9AD3B1B7"),
    TabData("Editor's Choice", contentTypeGhazal, "13ywdpLJ9iEA93BtCwez2w8zXFPWxoDota")
)

fun nazmTabData() = arrayOf(
    TabData("Top 100", contentTypeNazm, "0DBC9550-9833-4E30-A695-A63679966C69"),
    TabData("Beginners", contentTypeNazm, "BFCE2895-551E-4C07-B9D7-F7FF0DA9F9F5"),
    TabData("Editor's Choice", contentTypeNazm, "18BE4E82-1A8D-4750-B1FF-47B7E95BD458"),
    TabData("Humour/Satire", contentTypeNazm, "7DF9047A-D6B7-4CB0-8180-C29F9AD3B1B7")
)

fun poetTabData() = arrayOf(
    TabData("Young Poets", contentTypePoet, "B16B3E22-7881-4FE3-B2BD-94D45CC47139"),
    TabData("Top Read", contentTypePoet, "3AB1ACC4-E080-4B77-BFC1-41E2908E6C2D"),
    TabData("Women Poets", contentTypePoet, "9F15FCDD-C531-4EB9-A25E-F2F7DB8C7CF3"),
    TabData("Classical Poets", contentTypePoet, "517093F6-D295-4389-AA02-AEA52E738B97")
)

fun searchTabData() = arrayOf(
    TabData("All", contentTypePoet, "B16B3E22-7881-4FE3-B2BD-94D45CC47139"),
    TabData("Ghazal", contentTypePoet, "3AB1ACC4-E080-4B77-BFC1-41E2908E6C2D"),
    TabData("Nazm", contentTypePoet, "9F15FCDD-C531-4EB9-A25E-F2F7DB8C7CF3"),
    TabData("Sher", contentTypePoet, "517093F6-D295-4389-AA02-AEA52E738B97"),
    TabData("Dictionary", contentTypePoet, "517093F6-D295-4389-AA02-AEA52E738B97")
)

fun profileTabData(poetId: String, ghazalCount: Int, nazmCount: Int) =
    arrayOf(
        TabData(
            "Profile",
            contentTypeProfile,
            null,
            1,
            poetId,
            ghazalCount,
            nazmCount
        ),
        TabData("Ghazal", contentTypeGhazal, null, 0, poetId, ghazalCount, nazmCount),
        TabData("Nazm", contentTypeNazm, null, 0, poetId, ghazalCount, nazmCount),
        TabData("Sher", contentTypeSher, null, 0, poetId, ghazalCount, nazmCount)
    )
