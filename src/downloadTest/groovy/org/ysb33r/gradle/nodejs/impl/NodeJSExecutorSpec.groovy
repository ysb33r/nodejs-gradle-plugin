package org.ysb33r.gradle.nodejs.impl

import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.GulpExtension
import org.ysb33r.gradle.nodejs.NodeJSExecSpec
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.helper.NpmBaseTestSpecification
import org.ysb33r.gradle.nodejs.impl.npm.NpmExecutor

class NodeJSExecutorSpec extends NpmBaseTestSpecification {

    void setup() {
        NpmExecutor.initPkgJson(project,project.extensions.nodejs,project.extensions.npm)
    }

    File setupGulpDownload(Project project) {
        project.allprojects {
            dependencies {
                npm npmPackage(name: 'gulp', tag: GulpExtension.GULP_DEFAULT)
            }
        }
        project.configurations.npm.resolve()
        NpmExtension npm = (NpmExtension)(project.extensions.npm)
        new File(npm.homeDirectory,'node_modules/gulp/bin/gulp.js')
    }

    def 'Download a file then execute it'() {
        setup:
        File gulpFile = setupGulpDownload(project)
        OutputStream output = new ByteArrayOutputStream()
        NpmExtension npm = (NpmExtension)(project.extensions.npm)
        NodeJSExtension nodejs = (NodeJSExtension)(project.extensions.nodejs)

        when:
        NodeJSExecSpec execSpec = new NodeJSExecSpec(project)
        execSpec.script gulpFile
        execSpec.scriptArgs '--help'
        execSpec.standardOutput output
        execSpec.workingDir npm.getHomeDirectory()

        // there is no Gulpfile; we know it is going to fail.
        execSpec.ignoreExitValue true

        NodeJSExecutor.configureSpecFromExtensions(execSpec,nodejs)

        project.allprojects {
            nodeexec execSpec
        }

        then:
        output.toString().contains('No gulpfile found')
    }

    def 'Run nodeexec via a closure'() {
        setup:
        setupGulpDownload(project)
        OutputStream output = new ByteArrayOutputStream()

        when:
        project.allprojects {

            // tag::nodeexec-with-closure[]
            nodeexec {
                script 'node_modules/gulp/bin/gulp.js' // <1>
                scriptArgs '--help' // <2>
                workingDir npm.homeDirectory // <3>
                executable nodejs.resolvedNodeExecutable // <4>
                // end::nodeexec-with-closure[]
                standardOutput output
                ignoreExitValue true
                // tag::nodeexec-with-closure[]
            }
            // end::nodeexec-with-closure[]
        }

        then:
        output.toString().contains('No gulpfile found')
    }

}