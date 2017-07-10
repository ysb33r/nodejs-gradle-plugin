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

package org.ysb33r.gradle.nodejs.downloadtest

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.nodejs.downloadtest.helper.DownloadTestSpecification
import org.ysb33r.gradle.nodejs.impl.npm.NpmExecutor

class NpmDependencySpec extends DownloadTestSpecification {

    Project project = ProjectBuilder.builder().build()

    def 'Declare a dependency and resolve it'() {

        given:
        project.allprojects {

           apply plugin : 'org.ysb33r.nodejs.npm'

           nodejs {
               executable version : DownloadTestSpecification.NODEJS_VERSION
           }

           // tag::declare-npm-dependency[]
            dependencies {
                npm npmPackage(name : 'stringz', tag : '0.2.2')
            }
           // end::declare-npm-dependency[]
        }

        when:
        NpmExecutor.initPkgJson(project,project.extensions.nodejs,project.extensions.npm)
        Set<File> files = project.configurations.getByName('npm').resolve()

        then:
        files.size() > 0
    }
}