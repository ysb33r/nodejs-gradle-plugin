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
import org.gradle.process.ExecSpec
import org.gradle.process.ProcessForkOptions
import org.ysb33r.gradle.nodejs.impl.npm.NpmDistributionResolver
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** Specification for running an NPM command via {@code npm-cli.js}
 *
 * <p> For simplicity Gradle executes {@code npm-cli.js} directly rather
 * than use yet another indirection of the {@code npm} shell script.
 *
 * @since 0.1
 */
@CompileStatic
class NpmExecSpec extends NpmDistributionResolver {

    /** Construct class and attach it to specific project.
     *
     * @param project Project this exec spec is attached.
     */
    NpmExecSpec(Project project) {
        super(project)
    }

    /** Do not use this tag of the method for NPM.
     *
     * @throw GradleException.
     */
    @Override
    ProcessForkOptions executable(Object o) {
        throw new GradleException( '''Use the Map tag to set the executable''' )
    }

    /** Do not use this tag of the method for NPM.
     *
     * @throw GradleException.
     */
    @Override
    void setExecutable(Object o) {
        throw new GradleException( '''Use the Map tag to set the executable''' )
    }

    /** Do not use this tag of the method for NPM.
     *
     * @throw GradleException.
     */
    @Override
    void setExecutable(String s) {
        throw new GradleException( '''Use the Map tag to set the executable''' )
    }

    /** Install a resolver to find {@code npm-cli.js}.
     *
     * @param resolver
     */
    @Override
    void setExecutable(ResolvedExecutable resolver) {
        super.setExecutable(resolver)
    }

    /** Install a resolver to find {@code npm-cli.js}.
     *
     * @param resolver @return This object as an instance of {@link org , gradle.process.ProcessForkOptions}
     */
    @Override
    ProcessForkOptions executable(ResolvedExecutable resolver) {
        return super.executable(resolver)
    }

    /** Install a resolver to find the {@code node} executable.
     *
     * @param resolver
     */
    void setNodeExecutable(ResolvedExecutable resolver) {
        this.nodeExecutable = resolver
    }

    /** Install a resolver to find the {@code node} executable.
     *
     * @param resolver
     */
    void nodeExecutable(ResolvedExecutable resolver) {
        setNodeExecutable(resolver)
    }

    /** Set the a method to discover {@code npm-cli.js}
     *
     * <pre>
     *     // Use a specific tag (gradle will install the tag if need be)
     *     executable tag : '1.2.3.4'
     *
     *     // Via a physical path
     *     executable path : '/path/to/npm-cli.js'
     *
     * <pre>
     *
     * @param exe A method to find {@code npm-cli.js} as described above.
     *
     * @return This object as an instance of {@link org.gradle.process.ProcessForkOptions}.
     */
    ProcessForkOptions executable(Map<String,Object> exe) {
        setExecutable(exe)
        return (ProcessForkOptions)this
    }

    /** Builds up the command-line.
     *
     * @return
     * @throw {@code GradleException} if executable is not set.
     */
    @Override
    protected List<String> buildCommandLine() {
        List<String> finalCmdLine = [
            nodeExecutable.executable.absolutePath,
        ]

        finalCmdLine.addAll super.buildCommandLine()

        finalCmdLine.addAll '--scripts-prepend-node-path', 'true'
        return finalCmdLine
    }

    /** Copies settings from this execution specification to a standard {@link org.gradle.process.ExecSpec}
     *
     * This method is intended to be called as late as possible by a project extension or a task which would want to delegate to
     * {@ project.exec} project extension. It will cause arguments to be evaluated.
     * The only items not immediately evaluated are {@code workingDir} and {@code executable}.
     *
     * @param execSpec Exec spec to configure.
     */
    @Override
    void copyToExecSpec(ExecSpec execSpec) {
        super.copyToExecSpec(execSpec)
        execSpec.setExecutable(nodeExecutable.executable.absolutePath)
    }

    private ResolvedExecutable nodeExecutable
}
