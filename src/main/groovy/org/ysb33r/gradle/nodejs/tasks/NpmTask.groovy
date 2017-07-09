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

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmExecSpec
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.impl.npm.NpmExecutor
import org.ysb33r.gradle.olifant.exec.AbstractCommandExecTask

/** Ability to execute any NPM command with parameters.
 *
 * <p> The task contains an NPM extension, which by default is setup to look at the
 * global NPM extension. It allows overriding on a per-task basis of NPM configuration.
 *
 * @since 0.1
 */
@CompileStatic
class NpmTask extends AbstractCommandExecTask<NpmExecSpec> {

    NpmTask() {
        super()
        npmExtension = (NpmExtension)(extensions.create(NpmExtension.NAME,NpmExtension,this))
        nodeExtension = (NodeJSExtension)(extensions.create(NodeJSExtension.NAME,NodeJSExtension,this))
    }

    /** Runs NPM task against an internal execution specification.
     *
     * <p> If a failure occurs and {@link #isIgnoreExitValue} is not set an exception will be raised.
     *
     * <p> Sets the {@code npm_config_userconfig} and {@code npm_config_globalconfig} before running from
     * either the local or global {@code npm} extension (in that order). It will also configure a {@code node}
     * executable using either the local or gloval {@code nodejs} extension.
     *
     */
    @Override
    void exec() {
        NpmExecSpec spec = getToolExecSpec()
        NpmExecutor.configureSpecFromExtensions(spec,nodeExtension,npmExtension)
        super.exec()
    }

    /** Factory method for creating an execution specification
     *
     * @param project Project that the execution specification should be associated to.
     * @return Execution Specification
     */
    @Override
    protected NpmExecSpec createExecSpec(Project project) {
        new NpmExecSpec(project)
    }

    private final NpmExtension npmExtension
    private final NodeJSExtension nodeExtension
}
