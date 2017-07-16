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

import org.ysb33r.gradle.nodejs.downloadtest.helper.DownloadTestSpecification
import org.ysb33r.gradle.nodejs.downloadtest.helper.NpmBaseTestSpecification
import org.ysb33r.gradle.nodejs.impl.npm.NpmExecutor
import org.ysb33r.gradle.nodejs.tasks.GulpTask

import java.nio.file.Files
import java.nio.file.StandardCopyOption

class GulpSpec extends NpmBaseTestSpecification {

    def 'Create task and run Gulp task'() {
        setup:
        File gulpScript = new File(project.projectDir,'gulpfile.js')
        Files.copy( new File(DownloadTestSpecification.RESOURCES_DIR,'simple-gulpfile.js').toPath(),gulpScript.toPath(),StandardCopyOption.COPY_ATTRIBUTES)

        project.allprojects {
            apply plugin : 'org.ysb33r.nodejs.gulp'
        }

        NpmExecutor.initPkgJson(project,project.nodejs,project.npm)

        GulpTask task = project.tasks.create('helloWorld',GulpTask) {
        }

        when:
        project.evaluate()
        task.execute()

        then:
        noExceptionThrown()
        
    }
}