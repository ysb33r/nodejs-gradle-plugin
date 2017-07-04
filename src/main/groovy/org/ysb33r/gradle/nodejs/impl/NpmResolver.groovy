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
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.ResolvedExecutable
import org.ysb33r.gradle.olifant.StringUtils
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable
import org.ysb33r.gradle.olifant.exec.ResolvedExecutableFactory

/**
 *
 * @since 0.1
 */
@CompileStatic
class NpmResolver extends NodeJSDistributionResolver {

    static final Map<String,Object> SEARCH_PATH = [ search : 'npm' ] as Map<String,Object>

    NpmResolver(Project project) {
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
            ((NodeJSExtension)from).resolvedNpmExecutable
        }
    }

    private static class Version implements ResolvedExecutableFactory {

        Version(Project project) {
            this.project = project
        }

        /** Creates {@link ResolvedExecutable} from a NPM version.
         *
         * @param options Ignored
         * @param from Anything convertible to a string that contains a valid NPM version.
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

//    static NpmResolver createFromOptions(final Map<String, ?> opts) {
//        Number keyCount = opts.keySet().count { String it ->
//            it == 'path' || it == 'search' || it == 'version' || it == 'default'
//        }
//
//        if (keyCount > 1) {
//            throw new GradleException('''Combining use of 'default', 'path', 'search' and 'version' is not valid.''')
//        }
//        if (opts['version']) {
//            return new Version(opts['version'])
//        }
//        if (opts['path']) {
//            return new Path(opts['path'])
//        }
//        if (opts['search']) {
//            return new SearchPath(opts['search'])
//        }
//        if (opts['default']) {
//            return new NodeDefault((NodeJSExtension)(opts['default']))
//        }
//
//    }
//
//    private static class Version extends NpmResolver {
//        Version(def lazyVersion) {
//            this.lazyVersion = lazyVersion
//        }
//
//        ResolvedExecutable resolve(Project project) {
////            Downloader dnl = new Downloader(StringUtils.stringize(lazyVersion),project)
////
////            return new ResolvedExecutable() {
////
////                @Override
////                File getExecutable() {
////                    dnl.getNodeExecutablePath()
////                }
////            }
//            null
//        }
//
//        private Object lazyVersion
//    }
//
//    private static class Path extends NpmResolver {
//        Path(def lazyPath) {
//            this.lazyPath = lazyPath
//        }
//
//        ResolvedExecutable resolve(Project project) {
//            File resolvedPath = project.file(lazyPath)
//
//            return new ResolvedExecutable() {
//                @Override
//                File getExecutable() {
//                    if(resolvedPath.exists()) {
//                        return resolvedPath
//                    } else {
//                        throw new GradleException("${resolvedPath} does not exist.")
//                    }
//                }
//            }
//        }
//
//        private Object lazyPath
//    }
//
//    private static class SearchPath extends NpmResolver {
//        SearchPath(def lazyPath) {
//            this.lazyPath = lazyPath
//        }
//
//        ResolvedExecutable resolve(Project project) {
//
//            return new ResolvedExecutable() {
//                @Override
//                File getExecutable() {
//                    org.ysb33r.gradle.nodejs.impl.SearchPath.forExecutable(lazyPath)
//                }
//            }
//        }
//
//        private Object lazyPath
//    }
//
//    private static class NodeDefault extends NpmResolver {
//
//        NodeDefault(NodeJSExtension ext) {
//            this.ext = ext
//        }
//        @Override
//        ResolvedExecutable resolve(Project project) {
//            ext.resolvedNpmExecutable
//        }
//
//        private NodeJSExtension ext
//    }
}
