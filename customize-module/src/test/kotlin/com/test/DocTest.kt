package cn.goour.test

import com.google.inject.Guice
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.Disposer
import org.jetbrains.dokka.*
import org.jetbrains.dokka.Utilities.DokkaAnalysisModule
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.config.JavaSourceRoot
import org.jetbrains.kotlin.config.ContentRoot
import org.jetbrains.kotlin.config.KotlinSourceRoot
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.util.*


class DocTest {

    @Test
    fun testAll() {
        val list = parsePerPackageOptions("cn.goour.test")
        println(list)
    }

    @Test
    fun testAll2() {
        val data = arrayOf(arrayOf("a", "b"), arrayOf("c", "d"), arrayOf("e", "f"))
        //Stream<String[]>
        val temp = Arrays.stream(data)
        //filter a stream of string[], and return a string[]?
        val stream = temp.filter { x -> "a" == x.toString() }
        stream.forEach {
            println(it)
        }
    }

    @Test
    fun testUser() {
        val path = "src/main"
//        verifyOutput(path, ".html", withKotlinRuntime = false) { model, output ->
//            buildPagesAndReadInto(model.members.single().members, output)
//        }
        val file = File(path)
        val roots = arrayOf(JavaSourceRoot(file, null), KotlinSourceRoot(path))
        val documentation = DocumentationModule("test")

        val options = DocumentationOptions(
            "out/customize-module-doc",
            "html",
            includeNonPublic = true,
            skipEmptyPackages = false,
            includeRootPackage = true,
            sourceLinks = listOf(),
            perPackageOptions = emptyList(),
            generateIndexPages = false,
            noStdlibLink = true,
            cacheRoot = "default",
            languageVersion = null,
            apiVersion = null
        )

        appendDocumentation(
            documentation, *roots,
            withJdk = true,
            withKotlinRuntime = false,
            options = options
        )
        documentation.prepareForGeneration(options)
        val list = LinkedList<DocumentationReference>()
        documentation.members.forEach {
            //            list.addAll(it.references(RefKind.Member))
            list.addAll(it.allReferences())
        }
        list.forEach {
            println(it.to.name)
        }
    }

    fun appendDocumentation(
        documentation: DocumentationModule,
        vararg roots: ContentRoot,
        withJdk: Boolean = false,
        withKotlinRuntime: Boolean = false,
        options: DocumentationOptions,
        defaultPlatforms: List<String> = emptyList()
    ) {
        val messageCollector = object : MessageCollector {
            override fun clear() {

            }

            override fun report(
                severity: CompilerMessageSeverity,
                message: String,
                location: CompilerMessageLocation?
            ) {
                when (severity) {
                    CompilerMessageSeverity.STRONG_WARNING,
                    CompilerMessageSeverity.WARNING,
                    CompilerMessageSeverity.LOGGING,
                    CompilerMessageSeverity.OUTPUT,
                    CompilerMessageSeverity.INFO,
                    CompilerMessageSeverity.ERROR -> {
                        println("$severity: $message at $location")
                    }
                    CompilerMessageSeverity.EXCEPTION -> {
                        Assert.fail("$severity: $message at $location")
                    }
                }
            }

            override fun hasErrors() = false
        }

        val environment = AnalysisEnvironment(messageCollector)
        environment.apply {
            if (withJdk || withKotlinRuntime) {
                val stringRoot = PathManager.getResourceRoot(String::class.java, "/java/lang/String.class")
                addClasspath(File(stringRoot))
            }
            if (withKotlinRuntime) {
                val kotlinStrictfpRoot = PathManager.getResourceRoot(Strictfp::class.java, "/kotlin/jvm/Strictfp.class")
                addClasspath(File(kotlinStrictfpRoot))
            }
            addRoots(roots.toList())

            loadLanguageVersionSettings(options.languageVersion, options.apiVersion)
        }
        val defaultPlatformsProvider = object : DefaultPlatformsProvider {
            override fun getDefaultPlatforms(descriptor: DeclarationDescriptor) = defaultPlatforms
        }
        val injector = Guice.createInjector(
            DokkaAnalysisModule(
                environment,
                options,
                defaultPlatformsProvider,
                documentation.nodeRefGraph,
                DokkaConsoleLogger
            )
        )
        buildDocumentationModule(injector, documentation)
        Disposer.dispose(environment)
    }
}