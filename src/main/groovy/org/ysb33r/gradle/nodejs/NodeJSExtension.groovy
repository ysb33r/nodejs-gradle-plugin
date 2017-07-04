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

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.impl.NodeJSDistributionResolver
import org.ysb33r.gradle.olifant.OperatingSystem
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** Configure project defaults for Node.js.
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSExtension {

    static final String NAME = 'nodejs'

    /** Constructs a new exception which is attahced to the provided project.
     *
     * @param project Project this extensionm is associated with.
     */
    NodeJSExtension(Project project) {
        this.project = project
        this.nodeResolver = new NodeJSDistributionResolver(project)
    }

    /** Sets node executable.
     *
     * It can be passed by a single map option.
     *
     * <code>
     *   // By version (Gradle will download and cache the correct distribution).
     *   executable version : '7.10.0'
     *
     *   // By a physical path (
     *   executable path : '/path/to/node'
     *
     *   // By using searchPath (will attempt to locate in search path).
     *   executable searchPath()
     * </code>
     *
     * @param opts Map taken {@code version} or {@code path} as key.
     */
    void executable( final Map<String,Object> opts ) {
        this.nodeResolver.executable(opts)
    }

    /** Resolves a path to a {@code node} executable.
     *
     * @return Returns the path to the located {@code node} executable.
     * @throw {@code GradleException} if executable was not configured.
     */
    ResolvedExecutable getResolvedNodeExecutable() {
        ResolvedExecutable ret = nodeResolver.getResolvedExecutable()

        if(ret == null) {
            throw new GradleException('Node.js executable has not been configured.')
        }
    }

    /** Resolves a path to a {@code npm} executable that is associated with the configured .
     *
     * @return Returns the path to the located {@code npm} executable.
     * @throw {@code GradleException} if executable was not configured.
     */
    ResolvedExecutable getResolvedNpmExecutable() {
        new ResolvedExecutable() {
            @Override
            File getExecutable() {
                File root = getResolvedNodeExecutable().getExecutable().getParentFile()
                if(OperatingSystem.current().windows) {
                    new File(root,'npm.cmd')
                } else {
                    new File(root,'npm')
                }
            }
        }
    }

    /** Use this to configure a system path search for Node
     *
     * @return Returns a special option to be used in {@link #executable}
     */
    static Map<String,Object> searchPath() {
        NodeJSDistributionResolver.SEARCH_PATH
    }

    private final Project project
    @PackageScope final NodeJSDistributionResolver nodeResolver



}
