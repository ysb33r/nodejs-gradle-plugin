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

package org.ysb33r.gradle.nodejs.impl

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmDependencyGroup
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.NpmPackageDescriptor
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** Resolves an NPM package and installs it in a designated node_modules directory
 *
 * @since 0.1
 */
@CompileStatic
class NpmPackageResolver {

    NpmPackageResolver(Project project, NodeJSExtension nodeExtension, NpmExtension npmExtension) {
        this.project = project
        this.node = nodeExtension
        this.npm = npmExtension
    }

    Set<File> resolve(final NpmPackageDescriptor npmPackageDescriptor, final NpmDependencyGroup installGroup ) {
        NpmExecutor.installNpmPackage(
            project,
            node,
            npm,
            npmPackageDescriptor,
            installGroup ?: NpmDependencyGroup.PRODUCTION,
            [] as List<String>
        )
    }

    final Project project
    final NodeJSExtension node
    final NpmExtension npm
}
