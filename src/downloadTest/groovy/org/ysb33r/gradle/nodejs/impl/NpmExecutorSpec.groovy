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

package org.ysb33r.gradle.nodejs.impl

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.nodejs.NpmDependency
import org.ysb33r.gradle.nodejs.NpmDependencyGroup
import org.ysb33r.gradle.nodejs.helper.DownloadTestSpecification
import org.ysb33r.gradle.nodejs.impl.npm.NpmExecutor
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.StandardCopyOption

class NpmExecutorSpec extends DownloadTestSpecification {

    Project project = ProjectBuilder.builder().build()

    void setup() {
        Downloader.baseURI = NODEJS_CACHE_DIR.toURI()

        project.allprojects {
            apply plugin: 'org.ysb33r.nodejs.npm'

            nodejs {
                executable version: '7.10.0'
            }

            npm {

            }
        }
    }


    @Unroll
    def 'Install NPM dependency as #group'() {
        when:
        File pkgJson = NpmExecutor.initPkgJson(project,project.extensions.nodejs,project.extensions.npm)

        Set<File> files = NpmExecutor.installNpmPackage(
            project,
            new NpmDependency('stringz','0.2.2'),
            group,
            []
        )
        File pkgroot = new File(project.projectDir,'node_modules/stringz')

        then:
        pkgroot.exists()
        files.size() > 20
        searchFor.empty ? true : pkgJson.text.contains(searchFor)

        where:
        group                          | searchFor
        NpmDependencyGroup.PRODUCTION  | ''
        NpmDependencyGroup.DEVELOPMENT | '"devDependencies":'
        NpmDependencyGroup.OPTIONAL    | '"optionalDependencies"'
    }

    def 'Install a set of dependencies from a package.json file'() {
        setup:
        File packageJson = new File(project.projectDir,'package.json')
        Files.copy( new File(RESOURCES_DIR,'installtest-package.json').toPath(),packageJson.toPath(),StandardCopyOption.COPY_ATTRIBUTES)

        when:
        NpmExecutor.installPackagesFromDescription(
            project,
            packageJson,
            []
        )

        then:
        new File(project.projectDir,'node_modules/websocket-extensions').exists()
        new File(project.projectDir,'node_modules/lru-cache').exists()
    }
}