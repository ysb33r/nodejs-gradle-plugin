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

/** Specifies various characteristics of an NPM Package which can be used for various
 * NPM commands, especially {@code install} and {@code search}.
 *
 * @since 0.1
 */
@CompileStatic
interface NpmPackageDescriptor {

    /** Returns a string that can be passed for installation purposes.
     *
     * @return Package name
     */
    String toString()

    /** Name of the package without tags
     *
     * @return NPM name
     */
    String getPackageName()

    /** Name of NPM tag
     *
     * @return NPM tag (or {@code null} if not defined).
     */
    String getTagName()

    /** Package scope
     *
     * @return NPM scope  (or {@code null} if not defined).
     */
    String getScope()
}