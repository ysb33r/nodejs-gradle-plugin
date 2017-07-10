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

import org.gradle.api.GradleException

/** A simplistic NPM package descriptor
 *
 * @since 0.1
 */
class SimpleNpmPackageDescriptor implements NpmPackageDescriptor {

    final String packageName
    final String tagName
    final String scope

    /**
     *
     * @param scope Can be null
     * @param packageName Cannot be null.
     * @param tagName Can be null
     */
    SimpleNpmPackageDescriptor(final String scope,final String packageName, final String tagName) {

        if(packageName == null) {
            throw new GradleException("Cannot have a null package name")
        }
        this.packageName = packageName
        this.scope = scope
        this.tagName = tagName ?: 'latest'
    }

    @Override
    String toString() {
        scope ? "@${scope}/${packageName}@${tagName}" : "${packageName}@${tagName}"
    }
}
