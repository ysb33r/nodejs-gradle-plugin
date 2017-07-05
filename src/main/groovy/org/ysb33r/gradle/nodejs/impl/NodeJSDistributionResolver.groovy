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

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.ResolvedExecutable
import org.ysb33r.gradle.olifant.OperatingSystem
import org.ysb33r.gradle.olifant.StringUtils
import org.ysb33r.gradle.olifant.exec.AbstractToolExecSpec
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable
import org.ysb33r.gradle.olifant.exec.ResolvedExecutableFactory

/** Provides a way of resolving a Node.js distribution
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSDistributionResolver extends AbstractToolExecSpec {

    static final Map<String,Object> SEARCH_PATH = [ search : 'node' ] as Map<String,Object>

    /** Construct class and attach it to specific project.
     *
     * @param project Project this exec spec is attached.
     */

    NodeJSDistributionResolver(Project project) {
        super(project)
        registerExecutableKeyActions('version',new Version(project))
    }

    private static class Version implements ResolvedExecutableFactory {

        Version(Project project) {
            this.project = project
        }

        /** Creates {@link ResolvedExecutable} from a NodeJS version.
         *
         * @param options Ignored
         * @param from Anything convertible to a string that contains a valid Node.JS version.
         * @return The resolved executable.
         */
        @Override
        ResolvedExecutable build(Map<String, Object> options, Object from) {
            Downloader dnl = new Downloader(StringUtils.stringize(from),project)
            return new ResolvedExecutable() {
                @Override
                File getExecutable() {
                    dnl.getNodeExecutablePath()
                }
            }
        }

        private final Project project
    }

}
