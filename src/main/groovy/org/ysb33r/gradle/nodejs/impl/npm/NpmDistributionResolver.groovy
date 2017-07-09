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
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.impl.Downloader
import org.ysb33r.gradle.olifant.StringUtils
import org.ysb33r.gradle.olifant.exec.AbstractCommandExecSpec
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable
import org.ysb33r.gradle.olifant.exec.ResolvedExecutableFactory

/**
 *
 * @since 0.1
 */
@CompileStatic
class NpmDistributionResolver extends AbstractCommandExecSpec {

    static final Map<String,Object> SEARCH_PATH = [ search : 'npm' ] as Map<String,Object>

    NpmDistributionResolver(Project project) {
        super(project)
        registerExecutableKeyActions('version',new Version(project))
        registerExecutableKeyActions('default',new NodeDefault())
    }


    private static class NodeDefault implements ResolvedExecutableFactory {

        /** Creates {@link ResolvedExecutable} from a specific input.
         *
         * @param options Ignored
         * @param from An instance of {@link NodeJSExtension}
         * @return The resolved executable.
         */
        @Override
        ResolvedExecutable build(Map<String, Object> options, Object from) {
            ((NodeJSExtension)from).resolvedNpmCliJs
        }
    }

    private static class Version implements ResolvedExecutableFactory {

        Version(Project project) {
            this.project = project
        }

        /** Creates {@link ResolvedExecutable} from a NPM tag.
         *
         * @param options Ignored
         * @param from Anything convertible to a string that contains a valid NPM tag.
         * @return The resolved executable.
         */
        @Override
        ResolvedExecutable build(Map<String, Object> options, Object from) {
            Downloader dnl = new Downloader(StringUtils.stringize(from),project)
            return new ResolvedExecutable() {
                @Override
                File getExecutable() {
                    dnl.getNpmExecutablePath()
                }
            }
        }

        private final Project project
    }
}
