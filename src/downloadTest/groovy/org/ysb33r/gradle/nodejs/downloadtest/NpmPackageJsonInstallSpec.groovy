package org.ysb33r.gradle.nodejs.downloadtest

import org.gradle.api.Task
import org.ysb33r.gradle.nodejs.downloadtest.helper.DownloadTestSpecification
import org.ysb33r.gradle.nodejs.downloadtest.helper.NpmBaseTestSpecification
import org.ysb33r.gradle.nodejs.tasks.NpmPackageJsonInstall

import java.nio.file.Files
import java.nio.file.StandardCopyOption

class NpmPackageJsonInstallSpec extends NpmBaseTestSpecification {

    def 'Install a set of dependencies from a package.json file'() {
        setup:
        File packageJson = new File(project.projectDir,'package.json')
        Files.copy( new File(DownloadTestSpecification.RESOURCES_DIR,'installtest-package.json').toPath(),packageJson.toPath(),StandardCopyOption.COPY_ATTRIBUTES)
        Task installFiles = project.tasks.create('installFiles',NpmPackageJsonInstall)

        when:
        project.evaluate()
        installFiles.execute()

        then:
        new File(project.projectDir,'node_modules/websocket-extensions').exists()
        new File(project.projectDir,'node_modules/lru-cache').exists()
    }
}

