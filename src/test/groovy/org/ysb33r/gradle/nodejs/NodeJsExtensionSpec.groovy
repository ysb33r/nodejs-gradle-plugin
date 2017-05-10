package org.ysb33r.gradle.nodejs

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.nodejs.impl.NodeJSDistributionResolver
import spock.lang.Specification


class NodeJsExtensionSpec extends Specification {

    Project project = ProjectBuilder.builder().build()

    def 'Configure NodeJS executable using a version'() {

        when: 'A version is configured'
        project.allprojects {

            apply plugin : NodeJSBasePlugin

            // tag::configure-with-version[]
            nodejs {
                executable version : '7.10.0' // <1>
            }
            // end::configure-with-version[]
        }

        then:
        project.nodejs.resolvedDistribution != null
    }

    def 'Configure NodeJS executable using a path'() {

        when: 'A version is configured'
        project.allprojects {

            apply plugin : NodeJSBasePlugin

            // tag::configure-with-path[]
            nodejs {
                executable path : '/path/to/node' // <2>
            }
            // end::configure-with-path[]
        }

        then:
        project.nodejs.resolvedDistribution != null
    }

    def 'Configure NodeJS executable using a search path'() {

        when: 'A version is configured'
        project.allprojects {

            apply plugin : NodeJSBasePlugin

            // tag::configure-with-search-path[]
            nodejs {
                executable searchPath() // <3>
            }
            // end::configure-with-search-path[]
        }

        then:
        project.nodejs.resolvedDistribution != null
    }

    def 'NodeJS executable must be configured'() {

        when: 'No version is configured'
        project.apply NodeJSBasePlugin

        and: 'The executable path is requested'
        project.nodejs.getExecutable()

        then: 'An exception is raised'
        thrown(GradleException)
    }

    def 'Cannot configure NodeJS with more than one option'() {

        when:
        project.apply plugin : NodeJSBasePlugin
        project.nodejs.executable version : '7.10.0', path : '/path/to'

        then:
        thrown(GradleException)

        when:
        project.nodejs.executable version : '7.10.0', search : '/path/to'

        then:
        thrown(GradleException)

    }
}