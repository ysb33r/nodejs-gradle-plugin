package org.ysb33r.gradle.nodejs

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class NpmExtensionSpec extends Specification {

    Project project = ProjectBuilder.builder().build()

    void setup() {
        project.allprojects {
            apply plugin : NodeJSBasePlugin

            nodejs {
                executable version : '7.10.0'
            }
        }
    }

    def 'Use default NPM version'() {
        expect:
        project.npm.getResolvedNpmExecutable() != null
    }

    def 'Configure NPM executable version'() {

        when: 'A version is configured'
        project.allprojects {

            // tag::configure-with-version[]
            npm {
                executable version : '4.5.0' // <1>
            }
            // end::configure-with-version[]
        }

        then:
        project.nodejs.getResolvedNpmExecutable() != null
    }

    def 'Configure NPM executable using a path'() {

        when: 'A version is configured'
        project.allprojects {

            // tag::configure-with-path[]
            npm {
                executable path : '/path/to/npm' // <2>
            }
            // end::configure-with-path[]
        }

        then:
        project.npm.getResolvedNpmExecutable() != null
    }

    def 'Configure NPM executable using a search path'() {

        when: 'A version is configured'
        project.allprojects {

            apply plugin : NodeJSBasePlugin

            // tag::configure-with-search-path[]
            npm {
                executable searchPath() // <3>
            }
            // end::configure-with-search-path[]
        }

        then:
        project.npm.getResolvedNpmExecutable() != null
    }

    def 'Reset NPM to default version'() {

        when: 'The default version is configured'
        project.allprojects {

            // tag::configure-with-default[]
            npm {
                executable defaultNodejs() // <4>
            }
            // end::configure-with-default[]
        }

        then:
        project.nodejs.getResolvedNpmExecutable() != null
    }

    def 'Cannot configure NPM with more than one option'() {

        when:
        project.npm.executable version : '7.10.0', path : '/path/to'

        then:
        thrown(GradleException)

        when:
        project.npm.executable version : '7.10.0', search : '/path/to'

        then:
        thrown(GradleException)

    }
}