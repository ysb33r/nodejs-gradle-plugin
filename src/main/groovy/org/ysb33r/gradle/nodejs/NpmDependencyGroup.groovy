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

/** Defines the three NPM package installation/grouping options
 *
 * @since 0.1
 */
@CompileStatic
enum NpmDependencyGroup {
    PRODUCTION('prod'),
    DEVELOPMENT('dev'),
    OPTIONAL('optional')

    String getDependencyGroup() {
        this.dependencyGroup
    }

    private NpmDependencyGroup( final String installArg) {
        this.dependencyGroup = installArg
    }

    private final String dependencyGroup
}