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

package org.ysb33r.gradle.nodejs.tasks

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.ysb33r.gradle.nodejs.GulpExtension
import org.ysb33r.gradle.nodejs.NodeJSExecSpec
import org.ysb33r.gradle.nodejs.impl.NodeJSExecutor

/** Ability to run a Gulp task.
 *
 * <p> If the project defaults for {@code nodejs{, {@code npm} and {@code gulp} is not to requirement, each
 * of those can be configure via task extensions by the same name.
 *
 * @since 0.1
 */
@CompileStatic
class GulpTask extends AbstractNodeBaseTask {

    GulpTask() {
        super()
        gulpExtension = (GulpExtension)(extensions.create(GulpExtension.NAME,GulpExtension,this))

        inputs.property 'requires', {
            gulpExtension.requires.join(',')
        }
    }

    /** The task to run.
     *
     * <p> {@code target} is used as a keyword as opposed to {@code task} as the latter may clash
     *   with the common DSL keyword already in used in Gradle.
     *
     * @return Task to run or {@code null} is the default task neds to be run
     */
    @Optional
    @Input
    String getTarget() {
        this.taskToRun
    }

    /** Set the Gulp task to run.
     *
     * <p> {@code target} is used as a keyword as opposed to {@code task} as the latter may clash
     *   with the common DSL keyword already in used in Gradle.
     *
     * @param taskName
     */
    void setTarget(final String taskName) {
        this.taskToRun = taskName
    }

    /** The absolute path to {@code gulpfile.js} that will be used.
     *
     * @return Absolute path as a string
     */
    @Input
    String getGulpPath() {
        gulpExtension.gulpFile.absolutePath
    }

    /** The {@cde gulpfile.js} that will be used.
     *
     * @return Gulp file.
     */
    @InputFile
    File getGulpFile() {
        gulpExtension.gulpFile
    }

    @TaskAction
    void exec() {
        NodeJSExecSpec execSpec = createExecSpec()

        execSpec.script gulpExtension.resolvedExecutable.executable.absolutePath

        execSpec.scriptArgs '--gulpfile', getGulpPath()

        if(project.logging.level == LogLevel.QUIET) {
            execSpec.scriptArgs '--silent'
        }

        for( String req : gulpExtension.requires ) {
            execSpec.scriptArgs '--require', req
        }

        if(taskToRun) {
            execSpec.scriptArgs taskToRun
        }

        runExecSpec(execSpec)
    }

    private String taskToRun
    private final GulpExtension gulpExtension

}
