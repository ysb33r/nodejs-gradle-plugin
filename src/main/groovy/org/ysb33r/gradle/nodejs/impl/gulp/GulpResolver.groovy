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

package org.ysb33r.gradle.nodejs.impl.gulp

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.impl.npm.NpmExecutor
import org.ysb33r.gradle.olifant.exec.AbstractCommandExecSpec
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable
import org.ysb33r.gradle.olifant.exec.ResolvedExecutableFactory

/**
 *
 * @since 0.1
 */
@CompileStatic
class GulpResolver extends AbstractCommandExecSpec {

    GulpResolver(Project project) {
        super(project)
        registerExecutableKeyActions('version',new Version(project))
    }

    private static class Version implements ResolvedExecutableFactory {

        Version(Project project) {
            this.project = project
        }

        /** Creates {@link ResolvedExecutable} from a Gulp tag.
         *
         * @param options Ignored
         * @param from Anything convertible to a string that contains a valid Gulp tag.
         * @return The resolved executable.
         */
        @Override
        ResolvedExecutable build(Map<String, Object> options, Object from) {
//            Downloader dnl = new Downloader(StringUtils.stringize(from),project)
//            return new ResolvedExecutable() {
//                @Override
//                File getExecutable() {
//                    Set<File> files = NpmExecutor.installNpmPackage()
//                    dnl.getNpmExecutablePath()
//                }
//            }
        }

        private final Project project
    }
}
