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
import org.gradle.process.ExecResult
import org.gradle.testfixtures.ProjectBuilder
import org.ysb33r.gradle.nodejs.helper.DownloadTestSpecification
import spock.lang.IgnoreIf


class DownloaderSpec extends DownloadTestSpecification {

    Project project = ProjectBuilder.builder().build()

    @IgnoreIf({ DownloadTestSpecification.SKIP_TESTS })
    def "Download a NodeJS distribution" () {
        given: "A requirement to download NodeJS #NODEJS_VERSION"
        Downloader dwn = new Downloader(NODEJS_VERSION,project)
        dwn.downloadRoot = new File(project.buildDir,'download')
        dwn.baseURI = NODEJS_CACHE_DIR.toURI()

        when: "The distribution root is requested"
        File gotIt = dwn.distributionRoot

        String node = OS.windows ? 'node.exe' : 'bin/node'
        String npm = OS.windows ? 'npm.cmd' : 'bin/npm'

        then: "The distribution is downloaded and unpacked"
        gotIt != null
        new File(gotIt,node).exists()
        new File(gotIt,npm).exists()

        when: "The nodejs executable is run to display the help page"
        ExecResult result = project.exec {
            executable dwn.nodeExecutablePath
            args '-h'
        }

        then: "No runtime error is expected"
        result.assertNormalExitValue()
    }
}