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
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.impl.NodeJSDistributionResolver
import org.ysb33r.gradle.nodejs.impl.NpmResolver
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** Set up global config or task-based config for NPM.
 *
 * @since 0.1
 */
@CompileStatic
class NpmExtension {

    static final String NAME = 'npm'

    /** Adds the extension to the project.
     *
     * @param project Project to link to.
     */
    NpmExtension(Project project) {
        this.project = project
        this.npmResolver = new NpmResolver(project)
        npmResolver.executable(this.defaultNodejs())
    }

    /** Adds the extension to a {@link NpmTask} task.
     *
     * @param task Task to be extended.
     */
    NpmExtension(NpmTask task) {
        this.task = task
        this.npmResolver = new NpmResolver(task.project)
        npmResolver.executable(this.defaultNodejs())
    }

    /** Sets npm executable.
     *
     * It can be passed by a single map option.
     *
     * <code>
     *   // By version (Gradle will download and cache the correct distribution).
     *   executable version : '7.10.0'
     *
     *   // By a physical path (
     *   executable path : '/path/to/npm'
     *
     *   // By using searchPath (will attempt to locate in search path).
     *   executable searchPath()
     *
     *   // By using the the {@code npm} that is bundled by the
     *   executable defaultNodejs()
     * </code>
     *
     * If nothing is set the default will be {@pcde defaultNodejs ( )}
     *
     * @param opts Map taking one of the keys or methods mentioned above.
     */
    void executable(final Map<String, Object> opts) {
        npmResolver.executable(opts)
    }

    /** Resolves a path to a {@code node} executable.
     *
     * @return Returns the path to the located {@code node} executable.
     * @throw {@code GradleException} if executable was not configured.
     */
    ResolvedExecutable getResolvedNpmExecutable() {
        npmResolver.getResolvedExecutable()
    }

    /** Sets NPM to be resolved from the default node.js distribution associated with this project.
     *
     * @return Something that is suitable to be passed to @{@link #executable}
     */
    Map<String, Object> defaultNodejs() {
        ['default': ((NodeJSExtension) (getProject().extensions.getByName(NodeJSExtension.NAME)))] as Map<String, Object>
    }

    /** Use this to configure a system path search for {@code npm}
     *
     * @return Returns a special option to be used in {@link #executable}
     */
    Map<String, Object> searchPath() {
        NpmResolver.SEARCH_PATH
    }

    /** Obtains the project this is directly or indirectly attached to.
     *
     * @return Project instance. If this extension was attached to a task, the associated project for that task will be returned.
     */
    private Project getProject() {
        this.project ?: task.project
    }

    private Project project
    private NpmTask task
    private NpmResolver npmResolver
}