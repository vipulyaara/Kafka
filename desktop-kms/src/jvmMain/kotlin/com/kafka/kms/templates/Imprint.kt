package com.kafka.kms.templates

import com.kafka.kms.data.models.CopyrightType
import kotlinx.datetime.Clock

object Imprint {

    fun addImprint(path: String, copyrightType: CopyrightType) {
        val imprintContent = getImprintXhtml(copyrightType)
        java.io.File("$path/imprint.xhtml").apply {
            parentFile.mkdirs()
            writeText(imprintContent)
        }
    }

    private fun getImprintXhtml(copyrightType: CopyrightType) = """<?xml version="1.0" encoding="utf-8"?>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" epub:prefix="z3998: http://www.daisy.org/z3998/2012/vocab/structure/, se: https://standardebooks.org/vocab/1.0" xml:lang="en-US">
    <head>
        <title>Imprint</title>
        <link href="../css/core.css" rel="stylesheet" type="text/css"/>
        <link href="../css/kafka.css" rel="stylesheet" type="text/css"/>
    </head>
    <body epub:type="frontmatter">
        <section id="imprint" epub:type="imprint">
            <header>
                <h2 epub:type="title">Imprint</h2>
                <img alt="Your Company Logo" src="../images/logo.svg" epub:type="z3998:publisher-logo se:image.color-depth.black-on-transparent"/>
            </header>
            <p>This ebook was created with care and attention to detail by <a href="https://www.kafka.studio">Kafka Studio</a>. We strive to produce high-quality digital reading experiences while respecting intellectual property rights.</p>
            <p>This ebook is based on carefully curated source material, ensuring the highest standards of accuracy and presentation.</p>
            <p>${copyrightText(copyrightType)}</p>
           
            <p>Digital Publication Year - ${Clock.System.now().toString().substring(0, 4)}</p>
        </section>
    </body>
</html>"""
}

private fun copyrightText(copyrightType: CopyrightType): String = when (copyrightType) {
    CopyrightType.PublicDomain -> """
        This work is in the public domain in countries where the copyright term is the author's life plus 70 years or less. 
        This digital edition is carefully prepared to maintain the integrity of the original work while making it accessible 
        to modern readers.
    """.trimIndent()
    
    CopyrightType.GutenbergPublicDomain -> """
        This eBook is for the use of anyone anywhere in the United States and most other parts of the world at no cost and 
        with almost no restrictions whatsoever. You may copy it, give it away or re-use it under the terms of the Project 
        Gutenberg License included with this eBook or online at www.gutenberg.org. If you are not located in the United 
        States, you'll have to check the laws of the country where you are located before using this eBook.
    """.trimIndent()

    CopyrightType.GutenbergCopyright -> """
        This is a COPYRIGHTED Project Gutenberg eBook. Details Below.
        Please follow the copyright guidelines in this file or online at <a href="https://www.gutenberg.org">www.gutenberg.org</a>.
    """.trimIndent()

    CopyrightType.KafkaCopyrightOpen -> """
        © ${Clock.System.now().toString().substring(0, 4)} <a href="https://www.kafka.studio">Kafka Studio</a>. This digital edition is licensed under a Creative 
        Commons Attribution-NonCommercial 4.0 International License. You are free to share and adapt this work for 
        non-commercial purposes, provided you give appropriate credit to <a href="https://www.kafka.studio">Kafka Studio</a> and indicate if changes were made. 
        For more information about your rights and obligations under this license, visit 
        https://creativecommons.org/licenses/by-nc/4.0/.
    """.trimIndent()
    
    CopyrightType.KafkaCopyrightStrict -> """
        © ${Clock.System.now().toString().substring(0, 4)} <a href="https://www.kafka.studio">Kafka Studio</a>. All rights reserved. This digital edition is 
        protected by international copyright laws. No part of this publication may be reproduced, distributed, or transmitted 
        in any form or by any means without the prior written permission of <a href="https://www.kafka.studio">Kafka Studio</a>.
    """.trimIndent()
    
    CopyrightType.UnknownCopyright -> """
        The copyright status of this work is currently under review. While we believe this digital edition to be compliant 
        with applicable copyright laws, users are advised to verify the copyright status in their jurisdiction before any 
        commercial use or distribution.
    """.trimIndent()
    
    CopyrightType.UnknownStatus -> """
        The copyright status of this work could not be determined with certainty. This digital edition is provided for 
        educational and research purposes only. Users are responsible for ensuring their use complies with applicable 
        copyright laws in their jurisdiction.
    """.trimIndent()
}
