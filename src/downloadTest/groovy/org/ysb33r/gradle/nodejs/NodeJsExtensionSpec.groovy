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
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.nodejs.impl.Downloader
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable
import org.ysb33r.gradle.nodejs.helper.DownloadTestSpecification

class NodeJsExtensionSpec extends DownloadTestSpecification {
    Project project = ProjectBuilder.builder().build()


    void setup() {
        Downloader.baseURI = NODEJS_CACHE_DIR.toURI()

        project.allprojects {
            apply plugin : 'org.ysb33r.nodejs.base'

            // tag::configure-with-version[]
            nodejs {
                executable version : '7.10.0'
            }
            // end::configure-with-version[]
        }
    }

    def 'Resolving by version should download NodeJS'() {
        setup:
        ResolvedExecutable resolver = project.nodejs.getResolvedNodeExecutable()

        when:
        File nodeJSPath = resolver.executable

        then:
        nodeJSPath != null
        nodeJSPath.absolutePath.startsWith(new File(project.gradle.startParameter.gradleUserHomeDir,'native-binaries/nodejs').absolutePath)
    }
}