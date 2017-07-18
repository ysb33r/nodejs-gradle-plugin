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
import org.gradle.api.DefaultTask
import org.gradle.process.ExecResult
import org.ysb33r.gradle.nodejs.NodeJSExecSpec
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.impl.NodeJSExecutor

/** A base class that will provide NodeJS and NPM task extensions.
 *
 * @since 0.1
 */
@CompileStatic
class AbstractNodeBaseTask extends DefaultTask {

    protected AbstractNodeBaseTask() {
        super()
        npmExtension = (NpmExtension)(extensions.create(NpmExtension.NAME,NpmExtension,this))
        nodeExtension = (NodeJSExtension)(extensions.create(NodeJSExtension.NAME,NodeJSExtension,this))

        this.environment = [:]
        this.environment.putAll(NodeJSExecutor.defaultEnvironment)
    }

    void environment(Map<String,?> env) {
        this.environment.putAll(env)
    }

    void setEnvironment(Map<String,?> env) {
        this.environment.clear()
        this.environment.putAll(env)
    }

    Map<String,Object> getEnvironment() {
        this.environment
    }

    /** Internal access to attached NPM extension.
     *
      * @return NPM extension.
     */
    protected NpmExtension getNpmExtension() {
        this.npmExtension
    }

    /** Internal access to attached NodeJS extension.
     *
     * @return NodeJS extension.
     */
    protected NodeJSExtension getNodeExtension() {
        this.nodeExtension
    }

    /** Creates a node.js execution specification and poplates it with default
     * working directory, environment and location of {@code node}.
     *
     * @return Pre-configured execution specification.
     */
    protected NodeJSExecSpec createExecSpec() {
        NodeJSExecSpec execSpec = new NodeJSExecSpec(project)
        NodeJSExecutor.configureSpecFromExtensions(execSpec,nodeExtension)
        execSpec.workingDir(npmExtension.homeDirectory)
        execSpec.setEnvironment(this.environment)
        return execSpec
    }

    /** Executes a configured execution specification.
     *
     * @param execSpec Configured specification.
     * @return Execution result
     */
    protected ExecResult runExecSpec(NodeJSExecSpec execSpec) {
        run(execSpec)
    }

    @CompileDynamic
    private ExecResult run(NodeJSExecSpec execSpec) {
        project.nodeexec execSpec
    }


    private final NpmExtension npmExtension
    private final NodeJSExtension nodeExtension
    private final Map<String,Object> environment
}
