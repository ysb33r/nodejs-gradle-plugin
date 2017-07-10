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

package org.ysb33r.gradle.nodejs.pkgwrapper

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmDependencyGroup
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** A base class that can be used to wrap specific NPM packages as features in Gradle,
 *   thereby allowing for configuraton both a project or task level.
 *
 * @since 0.1
 */
@CompileStatic
abstract class AbstractPackageWrappingExtension<R extends  AbstractPackageResolver> {

    /** Sets wrapper executable.
     *
     * It can be passed by a single map option.
     *
     * <code>
     *   // By tag (Gradle will download and cache the correct distribution).
     *   executable tag : '3.9.1'
     *
     *   // By a physical path (
     *   executable path : '/path/to/package-file.js'
     * </code>
     *
     * @param opts Map taking one of the keys or methods mentioned above.
     */
    void executable(final Map<String, Object> opts) {
        if(this.resolver == null) {
            setResolver(createResolver())
        }
        resolver.executable(opts)
    }

    /** Lazy-evaluated location of the wrapepd package entry point.
     *
     * @return Resolved executable for the specific package.
     */
    ResolvedExecutable getResolvedExecutable() {
        getResolver().getResolvedExecutable(getEntryPoint(),getInstallGroup())
    }

    /** Sets the installation group (production, development, optional).
     *
     * @param installGroup
     */
    void setInstallGroup(NpmDependencyGroup installGroup) {
        this.installGroup = installGroup
    }

    /** The installation group for this package.
     *
     * @return The configured instalaltion group.
     *   If this is a task extension and it has not been configured, it will query the equivalent project extension.
     *   For a project extension that is not configured, {@code NpmDependencyGroup.PRODUCTION} will be returned
     */
    NpmDependencyGroup getInstallGroup() {
        if(this.task) {
            this.installGroup ?: ((AbstractPackageWrappingExtension)(getProject().extensions.getByName(getExtensionName()))).installGroup
        } else {
            this.installGroup
        }
    }
    /** Associate extension with a project.
     *
     * @param project
     */
    protected AbstractPackageWrappingExtension(Project project) {
        this.project = project
        this.installGroup = NpmDependencyGroup.PRODUCTION
    }

    /** Associate extension with a task.
     *
     * @param task
     */
    protected AbstractPackageWrappingExtension(Task task) {
        this.task = task
    }

    /** Obtain resolver that can be used to resolve the entry point to the package.
     *
     * @return Resolver instance
     * @throw GradleException if resolver is not configured at project level.
     */
    protected R getResolver() {
        if(this.task) {
            if(this.resolver) {
                this.resolver
            } else {
                AbstractPackageWrappingExtension ext = (AbstractPackageWrappingExtension)(getProject().extensions.getByName(getExtensionName()))
                ext.getResolver()
            }
        } else {
            if(this.resolver == null) {
                throw new GradleException( "No package resolver has been configured for '${getExtensionName()}' extension")
            }
            this.resolver
        }
    }

    /** Sets the resolver
     *
     * @param resolver Executable path resolver.
     */
    protected void setResolver(R resolver) {
        this.resolver = resolver
    }

    /** Obtains the project this is directly or indirectly attached to.
     *
     * @return Project instance. If this extension was attached to a task, the associated project for that task will be returned.
     */
    protected Project getProject() {
        this.project ?: this.task.project
    }

    /** Task associated with this extension
     *
     * @return Task if this is a task extension, null if it is a project extension.
     */
    protected Task getTask() {
        this.task
    }

    /** {@link NpmExtension} associated with the project or task extension
     *
     * @return {@link NpmExtension}
     */
    protected NpmExtension getNpmExtension() {
        ExtensionContainer ext = this.task ? this.task.extensions : this.project.extensions
        (NpmExtension)(ext.getByName(NpmExtension.NAME))
    }

    /** {@link NodeJSExtension} associated with the project or task extension
     *
     * @return {@link NodeJSExtension}
     */
    protected NodeJSExtension getNodeJSExtension() {
        ExtensionContainer ext = this.task ? this.task.extensions : this.project.extensions
        (NodeJSExtension)(ext.getByName(NodeJSExtension.NAME))
    }

    /** The entrypoint path relative to the installed package folder
     *
     * @return Relative path
     */
    abstract protected String getEntryPoint()

    /** Returns the name by which the extension is known.
     *
     * @return Extension name. Never null.
     */
    abstract protected String getExtensionName()

    /** Creates a resolver for this specific extension
     *
     * @return Resolver
     */
    abstract protected R createResolver()

    private Project project
    private Task task
    private R resolver
    private NpmDependencyGroup installGroup
}
