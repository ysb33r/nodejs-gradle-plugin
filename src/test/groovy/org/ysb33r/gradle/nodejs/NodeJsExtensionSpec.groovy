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


class NodeJsExtensionSpec extends Specification {

    Project project = ProjectBuilder.builder().build()

    def 'Configure NodeJS executable using a version'() {

        when: 'A tag is configured'
        project.allprojects {

            apply plugin : 'org.ysb33r.nodejs.base'

            // tag::configure-with-tag[]
            nodejs {
                executable version : '7.10.0' // <1>
            }
            // end::configure-with-tag[]
        }

        then:
        project.nodejs.getResolvedNodeExecutable() != null
    }

    def 'Configure NodeJS executable using a path'() {

        when: 'A tag is configured'
        project.allprojects {

            apply plugin : NodeJSBasePlugin

            // tag::configure-with-path[]
            nodejs {
                executable path : '/path/to/node' // <2>
            }
            // end::configure-with-path[]
        }

        then:
        project.nodejs.getResolvedNodeExecutable() != null
    }

    def 'Configure NodeJS executable using a search path'() {

        when: 'A tag is configured'
        project.allprojects {

            apply plugin : NodeJSBasePlugin

            // tag::configure-with-search-path[]
            nodejs {
                executable searchPath() // <3>
            }
            // end::configure-with-search-path[]
        }

        then:
        project.nodejs.nodeResolver != null
    }

    def 'NodeJS executable must be configured'() {

        when: 'No tag is configured'
        project.apply plugin : NodeJSBasePlugin

        and: 'The executable path is requested'
        project.nodejs.getResolvedNodeExecutable()

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