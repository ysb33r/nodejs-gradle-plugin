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

import org.gradle.api.artifacts.component.ComponentIdentifier

/** Implements a {@link org.gradle.api.artifacts.component.ComponentIdentifier} interface.
 *
 * @since 0.1
 */
class NpmComponentIdentifier implements ComponentIdentifier {

    NpmComponentIdentifier(final String name) {
        this.name = name
    }

    @Override
    String getDisplayName() {
        this.name
    }

    private final String name
}
