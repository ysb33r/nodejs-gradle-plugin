package org.ysb33r.gradle.nodejs.helper

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.nodejs.impl.Downloader

class NpmBaseTestSpecification extends DownloadTestSpecification {

    Project project = ProjectBuilder.builder().build()

    void setup() {
        Downloader.baseURI = NODEJS_CACHE_DIR.toURI()

        project.allprojects {
            apply plugin: 'org.ysb33r.nodejs.npm'

            nodejs {
                executable version: NODEJS_VERSION
            }

            npm {

            }
        }
    }

}