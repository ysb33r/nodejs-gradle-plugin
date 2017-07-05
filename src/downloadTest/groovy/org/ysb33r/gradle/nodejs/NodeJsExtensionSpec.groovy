package org.ysb33r.gradle.nodejs

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.nodejs.impl.Downloader
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable
import org.ysb33r.gradle.nodejs.helper.DownloadTestSpecification

class NodeJsExtensionSpec extends DownloadTestSpecification {
    Project project = ProjectBuilder.builder().build()


    void setup() {
        Downloader.baseURI = NODEJS_CACHE_DIR.toURI()

        project.allprojects {
            apply plugin : 'org.ysb33r.nodejs.base'

            nodejs {
                executable version : '7.10.0'
            }
        }
    }

    def 'Resolving by version should download NodeJS'() {
        setup:
        ResolvedExecutable resolver = project.nodejs.getResolvedNodeExecutable()

        when:
        File nodeJSPath = resolver.executable

        then:
        nodeJSPath != null
        nodeJSPath.absolutePath.startsWith(new File(project.gradle.startParameter.gradleUserHomeDir,'native-binaries/nodejs').absolutePath)
    }
}