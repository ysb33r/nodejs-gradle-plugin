//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2017
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================
//

package org.ysb33r.gradle.nodejs

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class NpmExtensionSpec extends Specification {

    Project project = ProjectBuilder.builder().build()

    void setup() {
        project.allprojects {
            apply plugin : 'org.ysb33r.nodejs.npm'

            nodejs {
                executable version : '7.10.0'
            }
        }
    }

    def 'Use default NPM version'() {
        expect:
        project.npm.getResolvedNpmCliJs() != null
    }

    def 'Configure NPM executable version'() {

        when: 'A tag is configured'
        project.allprojects {

            // tag::configure-with-tag[]
            npm {
                executable version : '4.5.0' // <1>
            }
            // end::configure-with-tag[]
        }

        then:
        project.nodejs.getResolvedNpmCliJs() != null
    }

    def 'Configure NPM executable using a path'() {

        when: 'A tag is configured'
        project.allprojects {

            // tag::configure-with-path[]
            npm {
                executable path : '/path/to/npm' // <2>
            }
            // end::configure-with-path[]
        }

        then:
        project.npm.getResolvedNpmCliJs() != null
    }

    def 'Configure NPM executable using a search path'() {

        when: 'A tag is configured'
        project.allprojects {

            apply plugin : NodeJSBasePlugin

            // tag::configure-with-search-path[]
            npm {
                executable searchPath() // <3>
            }
            // end::configure-with-search-path[]
        }

        then:
        project.npm.getResolvedNpmCliJs() != null
    }

    def 'Reset NPM to default version'() {

        when: 'The default tag is configured'
        project.allprojects {

            // tag::configure-with-default[]
            npm {
                executable defaultNodejs() // <4>
            }
            // end::configure-with-default[]
        }

        then:
        project.nodejs.getResolvedNpmCliJs() != null
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