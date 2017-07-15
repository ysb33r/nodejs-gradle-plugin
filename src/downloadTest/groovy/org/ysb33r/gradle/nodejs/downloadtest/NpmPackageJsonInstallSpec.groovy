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
        new File(project.projectDir,'node_modules/brace-expansion').exists()
        new File(project.projectDir,'node_modules/concat-map').exists()
    }
}

