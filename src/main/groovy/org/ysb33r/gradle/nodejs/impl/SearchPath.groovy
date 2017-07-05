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
import org.ysb33r.gradle.olifant.OperatingSystem
import org.ysb33r.gradle.olifant.StringUtils

/** Searches the operating system path for an executable
 *
 * @since 0.1
 */
@CompileStatic
class SearchPath {
    static private final OperatingSystem OS = OperatingSystem.current()

    /** Search path for executable
     *
     * @param path Any executable name supported by {@code org.ysb33r.gradle.olifant.StringUtils}.
     * @return Location of executable
     * @throw {@code GradleException} if not found
     */
    static File forExecutable(final Object lazyPath) {
        final String path = StringUtils.stringize(lazyPath)
        final File foundPath = OS.findInPath(path)

        if(foundPath == null) {
            throw new GradleException("Cannot locate '${path}' in system search path/")
        }

        foundPath
    }
}
