package com.test

import com.sampullara.cli.Args
import org.jetbrains.dokka.DokkaArguments
import org.jetbrains.dokka.MainKt.entry
import org.jetbrains.dokka.MainKt.startWithToolsJar

fun main() {

    val arguments = DokkaArguments()
    val args = arrayOf("src","customize-module/src/main","output","out/customize-module","format","javadoc")
    Args.parse(arguments, args)

    if (arguments.outputFormat == "javadoc")
        startWithToolsJar(args)
    else
        entry(args)
}