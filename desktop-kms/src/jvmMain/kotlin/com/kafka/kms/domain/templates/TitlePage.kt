package com.kafka.kms.domain.templates

import com.kafka.data.entities.ItemDetail

object TitlePage {

    fun addTitlePage(path: String, itemDetail: ItemDetail) {
        val imprintContent = getTitlePageXhtml(
            itemDetail.title,
            itemDetail.creators.orEmpty(),
            itemDetail.translators.orEmpty()
        )

        java.io.File("$path/titlepage.xhtml").apply {
            parentFile.mkdirs()
            writeText(imprintContent)
        }
    }

    private fun getTitlePageXhtml(
        title: String,
        authors: List<String>,
        translators: List<String>
    ): String = """<?xml version="1.0" encoding="utf-8"?>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" epub:prefix="z3998: http://www.daisy.org/z3998/2012/vocab/structure/, se: https://standardebooks.org/vocab/1.0" xml:lang="en-US">
    <head>
        <title>$title</title>
        <link href="../css/core.css" rel="stylesheet" type="text/css"/>
        <link href="../css/kafka.css" rel="stylesheet" type="text/css"/>
    </head>
    <body epub:type="frontmatter">
        <section id="titlepage" epub:type="titlepage">
            <h1 epub:type="title">$title</h1>
            ${if (authors.isNotEmpty()) """
            <p>by</p>
            ${authors.joinToString("\n") { """<p epub:type="z3998:author">$it</p>""" }}
            """ else ""}
            ${if (translators.isNotEmpty()) """
            <p>translated by</p>
            ${translators.joinToString("\n") { """<p epub:type="z3998:translator">$it</p>""" }}
            """ else ""}
            <img alt="Your Company Logo" src="../images/logo.svg" epub:type="z3998:publisher-logo se:image.color-depth.black-on-transparent"/>
        </section>
    </body>
</html>"""
}
