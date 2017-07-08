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

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.nodejs.helper.DownloadTestSpecification
import org.ysb33r.gradle.nodejs.impl.Downloader

class NpmTaskExecutionSpec extends DownloadTestSpecification {

    Project project = ProjectBuilder.builder().build()

    void setup() {
        Downloader.baseURI = NODEJS_CACHE_DIR.toURI()

        project.allprojects {
            apply plugin: 'org.ysb33r.nodejs.npm'

            nodejs {
                executable version: '7.10.0'
            }
        }
    }

    def 'Check configuration'() {
        setup:
        Task installer = project.tasks.create('config', NpmTask)
        OutputStream out = new ByteArrayOutputStream()
        installer.configure {
            command 'config'
            cmdArgs 'list'
            standardOutput out
        }

        when:
        project.evaluate()
        installer.execute()

        then:
        out.toString().contains('; cli configs')

    }

    def 'Install a package'() {
        setup:
        Task installer = project.tasks.create('installStringz', NpmTask)
        installer.configure {
            command 'install'
            cmdArgs 'stringz@0.2.2'
        }
        File node_modules = new File(project.projectDir, 'node_modules')

        when:
        project.evaluate()
        installer.execute()

        then:
        node_modules.exists()
        new File(node_modules, 'stringz').exists()
    }

}