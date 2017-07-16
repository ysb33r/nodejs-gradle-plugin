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
import org.gradle.api.Task
import org.ysb33r.gradle.nodejs.impl.NodeJSDistributionResolver
import org.ysb33r.gradle.olifant.OperatingSystem
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** Configure project defaults or task specifics for Node.js.
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSExtension {

    static final String NAME = 'nodejs'

    /** The default version of Node.js that will be used on
     * a supported platform if nothing else is configured.
     */
    static final String NODEJS_DEFAULT = '8.1.3'

    /** Constructs a new extension which is attached to the provided project.
     *
     * @param project Project this extensionm is associated with.
     */
    NodeJSExtension(Project project) {
        this.project = project
        this.nodeResolver = new NodeJSDistributionResolver(project)
        executable([ version : NODEJS_DEFAULT ] as Map<String,Object>)
    }

    /** Constructs a new extension which is attached to the provided task.
     *
     * @param project Project this extensionm is associated with.
     */
    NodeJSExtension(Task task) {
        this.task = task
        this.nodeResolver = new NodeJSDistributionResolver(project)
    }

    /** Sets node executable.
     *
     * It can be passed by a single map option.
     *
     * <code>
     *   // By tag (Gradle will download and cache the correct distribution).
     *   executable tag : '7.10.0'
     *
     *   // By a physical path (
     *   executable path : '/path/to/node'
     *
     *   // By using searchPath (will attempt to locate in search path).
     *   executable searchPath()
     * </code>
     *
     * @param opts Map taken {@code tag} or {@code path} as key.
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

        if(ret == null && task) {
            ret = ((NodeJSExtension)task.project.extensions.getByName(NAME)).getResolvedNodeExecutable()
        }
        if(ret == null) {
            throw new GradleException('Node.js executable has not been configured.')
        }

        ret
    }

    /** Resolves a path to a {@code npm} executable that is associated with the configured Node.js.
     *
     * @return Returns the path to the located {@code npm} executable.
     * @throw {@code GradleException} if executable was not configured.
     */
    ResolvedExecutable getResolvedNpmCliJs() {
        new ResolvedExecutable() {
            @Override
            File getExecutable() {
                File root
                if(OperatingSystem.current().windows) {
                    root= resolvedNodeExecutable.executable.parentFile
                } else {
                    root= new File(resolvedNodeExecutable.executable.parentFile.parentFile,'lib')
                }
                new File(root,'node_modules/npm/bin/npm-cli.js')
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
    private final Task task
    @PackageScope final NodeJSDistributionResolver nodeResolver
}
