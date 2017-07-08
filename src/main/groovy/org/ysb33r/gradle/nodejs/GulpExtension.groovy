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
import org.gradle.api.Project
import org.gradle.api.Task
import org.ysb33r.gradle.nodejs.impl.GulpResolver

/** Extension that allows for setting of Gulp configuration at a project or task level.
 * @since 0.1
 */
@CompileStatic
class GulpExtension {
    final static String NAME = 'gulpConfig'
    final static String GULP_DEFAULT = '3.9.1'

    /** Adds the extension to the project.
     *
     * <p> Sets the default Gulp tag to {@code GULP_DEFAULT}.
     *
     * @param project Project to link to.
     */
    GulpExtension(Project project) {
        this.project = project
        this.gulpResolver = new GulpResolver(project)
//        this.gulpResolver.executable( version : GULP_DEFAULT )
        this.workingDir = null
        this.gulpfile = "${project.projectDir}/gulpfile.js"
    }

    /** Adds the extension to a {@link GulpTask} task.
     *
     * <p> Links the executable resolving to the global instance, but allows
     * it to be overriden on a per-task basis.
     *
     * @param task Task to be extended.
     */
    GulpExtension(Task task) {
        this.task = task
    }

    /** Sets gulp executable.
     *
     * It can be passed by a single map option.
     *
     * <code>
     *   // By tag (Gradle will download and cache the correct distribution).
     *   executable tag : '3.9.1'
     *
     *   // By a physical path (
     *   executable path : '/path/to/gulp'
     * </code>
     *
     * If nothing is set the default will be to install {@link #GulpExtension.GULP_DEFAULT} for a project extension. In the case of
     * a task extension it will default to the project extension's settings.
     *
     * @param opts Map taking one of the keys or methods mentioned above.
     */
    void executable(final Map<String, Object> opts) {
        if(gulpResolver == null) {
            this.gulpResolver = new GulpResolver(getProject())
        }
        gulpResolver.executable(opts)
    }

    /** Obtains the project this is directly or indirectly attached to.
     *
     * @return Project instance. If this extension was attached to a task, the associated project for that task will be returned.
     */
    private Project getProject() {
        this.project ?: task.project
    }

    private Project project
    private Task task
    private GulpResolver gulpResolver
    private Object workingDir
    private Object gulpfile

}
