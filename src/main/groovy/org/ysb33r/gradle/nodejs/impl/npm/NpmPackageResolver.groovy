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

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmDependencyGroup
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.NpmPackageDescriptor

/** Resolves an NPM package and installs it in a designated node_modules directory
 *
 * @since 0.1
 */
@CompileStatic
class NpmPackageResolver {

    /** Creates a package resolver that as a NodeJS ane NPM context.
     *
     * @param project Project to which this resolver is associated.
     * @param nodeExtension NodeJS context
     * @param npmExtension NOPM context.
     */
    NpmPackageResolver(Project project, NodeJSExtension nodeExtension, NpmExtension npmExtension) {
        this.project = project
        this.node = nodeExtension
        this.npm = npmExtension
    }

    /** Resolves an NPM package
     *
     * @param npmPackageDescriptor A description of the package to be resolved.
     * @param installGroup Installation group. Can be {@code null}
     * @return A FileTree of the installed files.
     */
    FileTree resolve(final NpmPackageDescriptor npmPackageDescriptor, final NpmDependencyGroup installGroup ) {
        NpmExecutor.installNpmPackage(
            project,
            node,
            npm,
            npmPackageDescriptor,
            installGroup ?: NpmDependencyGroup.PRODUCTION,
            [] as List<String>
        )
    }

    /** Resolves an entry point and then finds the location of the entry point
     *
     * @param npmPackageDescriptor A description of the package to be resolved.
     * @param entryPoint Entry point of the package relative to installed package directory.
     *   For instance Gulp will be {@code bin/gulp.js}.
     * @param installGroup Installation group. Can be {@code null}
     * @return Location of the entry point
     * @throw GradleException If the entry point does not exist or multiple matches were found.
     */
    File resolvesWithEntryPoint(final NpmPackageDescriptor npmPackageDescriptor, final String entryPoint, final NpmDependencyGroup installGroup ) {
        FileTree tree = filterTree(
            resolve(npmPackageDescriptor,installGroup),
            entryPoint
        )

        if(tree.empty) {
            throw new GradleException("Package was installed, but could not find '${entryPoint}'")
        }

        tree.singleFile
    }

    @CompileDynamic
    private FileTree filterTree(FileTree tree,final String entryPoint) {
        tree.matching {
            include "**/${entryPoint}"
        }
    }

    private final Project project
    private final NodeJSExtension node
    private final NpmExtension npm
}
