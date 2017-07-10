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
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmDependencyGroup
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.SimpleNpmPackageDescriptor
import org.ysb33r.gradle.nodejs.impl.npm.NpmPackageResolver
import org.ysb33r.gradle.olifant.exec.AbstractToolExecSpec
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable
import org.ysb33r.gradle.olifant.exec.ResolvedExecutableFactory

/** A base class for creating resolver for specific NPM packages.
 *
 * @since 0.1
 */
@CompileStatic
class AbstractPackageResolver {

    void executable( Map<String,Object> opts) {
        factory.setExecutable(opts)
    }

    ResolvedExecutable getResolvedExecutable(final String entryPoint,final NpmDependencyGroup installGroup) {
        factory.entryPoint = entryPoint
        factory.installGroup = installGroup
        factory.resolvedExecutable
    }

    protected AbstractPackageResolver(final String scope,final String packageName,Project project, NodeJSExtension nodeExtension, NpmExtension npmExtension) {
        this.factory = new Factory(scope,packageName,project,nodeExtension,npmExtension)
    }

    private static class Version implements ResolvedExecutableFactory {

        Version(final Factory factory) {
            this.factory = factory
        }

        /** Creates {@link ResolvedExecutable} from a Gulp tag.
         *
         * @param options Ignored
         * @param from Anything convertible to a string that contains a valid NPM package tag.
         * @return The resolved executable.
         */
        @Override
        ResolvedExecutable build(Map<String, Object> options, Object from) {
            return new ResolvedExecutable() {
                @Override
                File getExecutable() {
                    SimpleNpmPackageDescriptor descriptor = new SimpleNpmPackageDescriptor(factory.scope,factory.packageName,from.toString())
                    factory.resolver.resolvesWithEntryPoint(descriptor,factory.entryPoint,factory.installGroup)
                }
            }
        }
        private Factory factory
    }

    private static class Factory extends AbstractToolExecSpec {
        Factory(final String scope, final String packageName, Project project, NodeJSExtension nodeExtension, NpmExtension npmExtension) {
            super(project)
            this.scope = scope
            this.packageName = packageName
            this.project = project
            this.resolver = new NpmPackageResolver(project,nodeExtension,npmExtension)

            registerExecutableKeyActions('version',new Version(this))
        }

        final String scope
        final String packageName
        final Project project
        final NodeJSExtension node
        final NpmExtension npm
        final NpmPackageResolver resolver

        NpmDependencyGroup installGroup
        String entryPoint
    }

    private final Factory factory

}
