package org.ysb33r.gradle.nodejs.helper

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class UnittestBaseSpecification extends Specification {

    final String NODEJS_VERSION = System.getProperty('NODEJS_VERSION') ?: '7.10.0'

    Project project = ProjectBuilder.builder().build()

    void setup() {
        project.allprojects {
            apply plugin : 'org.ysb33r.nodejs.npm'

            nodejs {
                executable version : NODEJS_VERSION
            }
        }
    }


}