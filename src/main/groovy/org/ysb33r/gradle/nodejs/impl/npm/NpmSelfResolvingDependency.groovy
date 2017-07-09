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

package org.ysb33r.gradle.nodejs.impl.npm

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.FileCollectionDependency
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.internal.artifacts.dependencies.SelfResolvingDependencyInternal
import org.gradle.api.internal.file.FileSystemSubset
import org.gradle.api.tasks.TaskDependency
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmDependency
import org.ysb33r.gradle.nodejs.NpmExtension

/** An NPM dependency that can resolve itself.
 *
 * @since 0.1
 */
@CompileStatic
class NpmSelfResolvingDependency extends NpmDependency implements FileCollectionDependency, SelfResolvingDependencyInternal {

    /** Allows additional parameters to be set
     *
     * @param project
     * @param properties
     */
    NpmSelfResolvingDependency(Project project,final Map<String,?> properties) {
        super(properties)
        this.project = project
    }

    NpmSelfResolvingDependency(
        Project project,
        NodeJSExtension nodeJSExtension,
        NpmExtension npmExtension,
        final Map<String,Object> properties
    ) {
        super(properties)
        this.project = project
        this.nodeJSExtension = nodeJSExtension
        this.npmExtension = npmExtension
    }

    @Override
    String getGroup() {
        this.scope
    }

    @Override
    String getName() {
        this.packageName
    }

    @Override
    String getVersion() {
        this.tagName == '+' ? 'latest' : this.tagName
    }

    @Override
    boolean contentEquals(Dependency dependency) {
        dependency instanceof NpmSelfResolvingDependency &&
            ((NpmSelfResolvingDependency)dependency).packageName == packageName &&
            ((NpmSelfResolvingDependency)dependency).tagName == tagName &&
            ((NpmSelfResolvingDependency)dependency).scope == scope
    }

    @Override
    Dependency copy() {
        Map<String,Object> pkg = [ name : packageName, tag : tagName, type : getInstallGroup() ] as Map<String,Object>
        if(scope) {
            pkg.put('scope', scope)
        }
        new NpmSelfResolvingDependency(
            project,
            nodeJSExtension,
            npmExtension,
            (Map<String,Object>)pkg
        )
    }


    @Override
    Set<File> resolve() {
        File installFolder = NpmExecutor.getPackageInstallationFolder(
            project,
            nodeJSExtension ?: project.extensions.getByType(NodeJSExtension),
            npmExtension ?: project.extensions.getByType(NpmExtension),
            this
        )



        NpmExecutor.installNpmPackage(
            project,
            nodeJSExtension ?: project.extensions.getByType(NodeJSExtension),
            npmExtension ?: project.extensions.getByType(NpmExtension),
            this,
            installGroup,
            [] as List<String>
        )
    }

    @Override
    Set<File> resolve(boolean b) {
        project.logger.warn("Ignoring transitive parameter for NPM")
        resolve()
    }

    @Override
    TaskDependency getBuildDependencies() {
        NO_TASK_DEPENDENCIES
    }

    @Override
    FileCollection getFiles() {
        FileTree fileTree = project.fileTree(NpmExecutor.getPackageInstallationFolder(
            project,
            nodeJSExtension ?: project.extensions.getByType(NodeJSExtension),
            npmExtension ?: project.extensions.getByType(NpmExtension),
            this
        ))

        if(fileTree.empty) {
            resolve()
        }

        fileTree
    }

    /** @deprecated */
    @Override
    void registerWatchPoints(FileSystemSubset.Builder builder) {
        project.logger.warn 'Ignoring watch point registry'
    }

    @Override
    ComponentIdentifier getTargetComponentId() {
        new NpmComponentIdentifier("NPM: ${this.toString()}")
    }

    private Project project
    private NodeJSExtension nodeJSExtension
    private NpmExtension npmExtension

    private static class NoTaskDependencies implements TaskDependency {

        static final Set EMPTY_SET = []

        @Override
        Set<? extends Task> getDependencies(Task task) {
            EMPTY_SET
        }
    }

    private static final NoTaskDependencies NO_TASK_DEPENDENCIES = new NoTaskDependencies()


}
